package planto_project.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import planto_project.dao.AccountRepository;
import planto_project.dto.RolesOfUserDto;
import planto_project.dto.UpdateUserDto;
import planto_project.dto.UserDto;
import planto_project.dto.UserRegisterDto;
import planto_project.exceptions.EmailAlreadyExistException;
import planto_project.exceptions.LoginAlreadyExistException;
import planto_project.model.Role;
import planto_project.model.UserAccount;
import planto_project.validator.UserValidator;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, CommandLineRunner {
    final AccountRepository accountRepository;
    final ModelMapper modelMapper;
    final UserValidator userValidator;
    final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!accountRepository.existsByRolesContaining(Set.of(Role.ADMINISTRATOR))) {
            String password = passwordEncoder.encode("1234");
            UserAccount user =
                    new UserAccount("admin", "admin", "admin", "email@email.com", password);
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
        UserAccount user = accountRepository.findUserByLogin(login);
        return modelMapper.map(user, UserDto.class);
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
    public UserDto getUser(String login) {
        return accountRepository.getUserAccountByLogin(login);
    }

    @Override
    public Set<UserDto> findAllUsers() {
        return accountRepository.findAll().stream()
                .map(u -> modelMapper.map(u, UserDto.class))
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
