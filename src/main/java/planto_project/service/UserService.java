package planto_project.service;

import jakarta.servlet.http.HttpServletResponse;
import planto_project.dto.*;
import planto_project.model.CartItem;

import java.util.Set;


public interface UserService {
    AuthResponseDto register(UserRegisterDto userRegisterDto, HttpServletResponse response);

    UserDto updateUser(String login, UpdateUserDto updateUserDto);

    UserDto removeUser(String login);

    UserDto findUserByLogin(String login);

    void changePassword(String login, String newPassword);

    RolesOfUserDto changeRolesList(String login, String role, boolean b);

    Set<UserDto> findAllUsers();

    Set<CartItemDto> addToCart(String login, String productId);

    Set<CartItemDto> removeFromCart(String login, String productId);

    Set<CartItemDto> getCartOfUser(String login);

    Set<CartItemDto> deleteProductFromCart(String login, String productId);

    void clearUserCart(String login);
}
