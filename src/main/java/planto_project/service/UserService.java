package planto_project.service;

import planto_project.dto.*;
import planto_project.model.CartItem;

import java.util.Set;


public interface UserService {
    UserDto register(UserRegisterDto userRegisterDto);

    UserDto updateUser(String login, UpdateUserDto updateUserDto);

    UserDto removeUser(String login);

    UserDto findUserByLogin(String login);

    void changePassword(String login, String newPassword);

    RolesOfUserDto changeRolesList(String login, String role, boolean b);

    Set<UserDto> findAllUsers();

    Set<CartItemDto> addToCart(String login, String productId);

    Set<CartItemDto> removeFromCart(String login, String productId);
}
