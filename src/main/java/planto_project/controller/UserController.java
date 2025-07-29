package planto_project.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import planto_project.dto.*;
import planto_project.service.UserService;

import java.security.Principal;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class UserController {
    final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody UserRegisterDto userRegisterDto,
                                                    HttpServletResponse response) {
        AuthResponseDto token = userService.register(userRegisterDto, response);
        return ResponseEntity.ok(token);
    }

    @GetMapping("users")
    public Set<UserDto> findAllUsers() {
        return userService.findAllUsers();
    }

    @PutMapping("user/{login}")
    public UserDto updateUser(@PathVariable String login, @RequestBody UpdateUserDto updateUserDto) {
        return userService.updateUser(login, updateUserDto);
    }

    @GetMapping("user/{login}")
    public UserDto findUserByLogin(@PathVariable String login) {
        return userService.findUserByLogin(login);
    }

    @DeleteMapping("user/{login}")
    public UserDto deleteUser(@PathVariable String login) {
        return userService.removeUser(login);
    }

    @PutMapping("/user/{login}/role/{role}")
    public RolesOfUserDto addRole(@PathVariable String login, @PathVariable String role) {
        return userService.changeRolesList(login, role, true);
    }

    @DeleteMapping("/user/{login}/role/{role}")
    public RolesOfUserDto removeRole(@PathVariable String login, @PathVariable String role) {
        return userService.changeRolesList(login, role, false);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(Principal principal, @RequestHeader("X-Password") String newPassword) {
        userService.changePassword(principal.getName(), newPassword);
    }

    @GetMapping("/user/{login}/cart")
    public Set<CartItemDto> getCartOfUser(@PathVariable String login) {
        return userService.getCartOfUser(login);
    }

    @PutMapping("/user/{login}/cart/{productId}")
    public Set<CartItemDto> addToCart(@PathVariable String login, @PathVariable String productId) {
        return userService.addToCart(login, productId);
    }

    @DeleteMapping("/user/{login}/cart/{productId}")
    public Set<CartItemDto> removeFromCart(@PathVariable String login, @PathVariable String productId) {
        return userService.removeFromCart(login, productId);
    }



}
