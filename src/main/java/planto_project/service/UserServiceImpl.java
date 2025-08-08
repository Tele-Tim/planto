package planto_project.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import planto_project.dao.AccountRepository;
import planto_project.dao.OrderRepository;
import planto_project.dao.ProductRepository;
import planto_project.dao.RefreshTokenRepository;
import planto_project.dto.*;
import planto_project.exceptions.EmailAlreadyExistException;
import planto_project.exceptions.LoginAlreadyExistException;
import planto_project.model.*;
import planto_project.security.JwtUtil;
import planto_project.security.MyHasher;
import planto_project.validator.UserValidator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, CommandLineRunner {
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;


    @Value("${app.jwt.refresh-token-expiration-days}")
    private int refreshTokenExpirationDays;

    @Value("${app.jwt.refresh-cookie-name}")
    private String refreshCookieName;

    @Override
    public void run(String... args) {
        if (!accountRepository.existsByRolesContaining(Set.of(Role.ADMINISTRATOR))) {
            String password = passwordEncoder.encode("1234");
            UserAccount user =
                    new UserAccount("admin", password, new ArrayList<>(), new HashSet<>());
            user.addRole(Role.ADMINISTRATOR.name());
            user.addRole(Role.MODERATOR.name());
            accountRepository.save(user);
        }
    }

    @Override
    public AuthResponseDto register(UserRegisterDto userRegisterDto, HttpServletResponse response) {
        userValidator.checkLogin(userRegisterDto.getLogin());
        userValidator.checkEmail(userRegisterDto.getEmail());

        if (accountRepository.existsByLogin(userRegisterDto.getLogin())) {
            throw new LoginAlreadyExistException();
        }

        if (accountRepository.existsByEmail(userRegisterDto.getEmail())) {
            throw new EmailAlreadyExistException();
        }

        UserAccount user = modelMapper.map(userRegisterDto, UserAccount.class);
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        accountRepository.save(user);

        String accessToken = jwtUtil.generateJwtToken(user.getLogin());
        String refreshToken = jwtUtil.generateRefreshToken(user.getLogin());
        String tokenHash = MyHasher.sha512Hex(refreshToken);

        refreshTokenRepository.save(RefreshToken.builder()
                .tokenHash(tokenHash)
                .expiryDate(Instant.now().plus(refreshTokenExpirationDays, ChronoUnit.DAYS))
                .revoked(false)
                .userLogin(user.getLogin())
                .build());

        Cookie cookie = new Cookie(refreshCookieName, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge((int) ChronoUnit.DAYS.getDuration().multipliedBy(refreshTokenExpirationDays).getSeconds());
        response.addCookie(cookie);

        return new AuthResponseDto(accessToken, jwtUtil.getJwtExpirationMs());
    }

    @Override
    public UserDto findUserByLogin(String login) {
        UserAccount user = accountRepository.findById(login).orElseThrow(() ->
                new UsernameNotFoundException("User with name " + login + " not found"));
        UserDto userDto = modelMapper.map(user, UserDto.class);
        List<Order> orders = orderRepository.findAllByUser_Login(login);
        List<OrderResponseDto> ordersDto = orders.stream()
                .map(o -> modelMapper.map(o, OrderResponseDto.class))
                .toList();
        userDto.setOrders(ordersDto);
        return userDto;
    }

    @Override
    @Transactional
    public RolesOfUserDto changeRolesList(String login, String role, boolean isAddRole) {
        UserAccount user = accountRepository.findUserByLogin(login);
        if (isAddRole) {
            user.addRole(role);
        } else {
            user.removeRole(role);
        }
        accountRepository.save(user);
        return modelMapper.map(user, RolesOfUserDto.class);
    }

    @Override
    public Set<UserDto> findAllUsers() {
        return accountRepository.findAll().stream()
                .map(u -> modelMapper.map(u, UserDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<CartItemDto> addToCart(String login, String productId) {
        UserAccount user = accountRepository.findById(login)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (user.getCart() == null) {
            user.setCart(new HashSet<>());
        }

        Optional<CartItem> existingItem = user.getCart().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            if (existingItem.get().getQuantity() >= product.getQuantity()) {
                throw new RuntimeException("Not enough items in stock");
            }
            existingItem.get().incrementItem();
        } else {
            user.getCart().add(new CartItem(productId));
        }

        accountRepository.save(user);

        return user.getCart().stream()
                .map(i -> modelMapper.map(i, CartItemDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<CartItemDto> removeFromCart(String login, String productId) {
        UserAccount user = accountRepository.findById(login)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getCart() == null || user.getCart().isEmpty()) {
            return Set.of();
        }

        Optional<CartItem> existingItem = user.getCart().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        existingItem.ifPresent(item -> {
            if (item.getQuantity() > 1) {
                item.decrementItem();
            } else {
                user.getCart().remove(item);
            }
        });

        accountRepository.save(user);

        return user.getCart().stream()
                .map(i -> modelMapper.map(i, CartItemDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<CartItemDto> getCartOfUser(String login) {
        UserAccount user = accountRepository.findById(login)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getCart().stream()
                .map(i -> modelMapper.map(i, CartItemDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<CartItemDto> deleteProductFromCart(String login, String productId) {
        UserAccount user = accountRepository.findById(login)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getCart() == null || user.getCart().isEmpty()) {
            return Set.of();
        }

        user.getCart().removeIf(i -> i.getProductId().equals(productId));

        accountRepository.save(user);

        return user.getCart().stream()
                .map(i -> modelMapper.map(i, CartItemDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public void clearUserCart(String login) {
        UserAccount user = accountRepository.findById(login)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user != null) {
            Set<CartItem> userCart = user.getCart();
            userCart.clear();
            accountRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }

    }

    @Override
    @Transactional
    public void changePassword(String login, String newPassword) {
        UserAccount user = accountRepository.findUserByLogin(login);
        String password = passwordEncoder.encode(newPassword);
        user.setPassword(password);
        accountRepository.save(user);
    }


    @Override
    @Transactional
    public UserDto updateUser(String login, UpdateUserDto updateUserDto) {
        UserAccount user = accountRepository.findUserByLogin(login);
        userValidator.updateFields(user, updateUserDto);
        accountRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto removeUser(String login) {
        UserAccount user = accountRepository.findUserByLogin(login);
        accountRepository.delete(user);
        return modelMapper.map(user, UserDto.class);
    }
}
