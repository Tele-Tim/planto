package planto_project.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import planto_project.dao.AccountRepository;
import planto_project.dao.OrderRepository;
import planto_project.dao.ProductRepository;
import planto_project.dto.*;
import planto_project.exceptions.EmailAlreadyExistException;
import planto_project.exceptions.LoginAlreadyExistException;
import planto_project.model.*;
import planto_project.validator.UserValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, CommandLineRunner {
    final AccountRepository accountRepository;
    final ModelMapper modelMapper;
    final UserValidator userValidator;
    final PasswordEncoder passwordEncoder;
    final OrderRepository orderRepository;
    final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!accountRepository.existsByRolesContaining(Set.of(Role.ADMINISTRATOR))) {
            String password = passwordEncoder.encode("1234");
            UserAccount user =
                    new UserAccount("admin", password);
            user.addRole(Role.ADMINISTRATOR.name());
            user.addRole(Role.MODERATOR.name());
            accountRepository.save(user);
        }
    }

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        userValidator.checkLogin(userRegisterDto.getLogin());
        userValidator.checkEmail(userRegisterDto.getEmail());
        if (accountRepository.existsByLogin((userRegisterDto.getLogin()))) {
            throw new LoginAlreadyExistException();
        }
        if (accountRepository.existsByEmail(userRegisterDto.getEmail())) {
            throw new EmailAlreadyExistException();
        }
        UserAccount user = modelMapper.map(userRegisterDto, UserAccount.class);
        String password = passwordEncoder.encode(userRegisterDto.getPassword());
        user.setPassword(password);
        accountRepository.save(user);
        return modelMapper.map(user, UserDto.class);
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
