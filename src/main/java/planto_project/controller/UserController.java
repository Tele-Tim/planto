package planto_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import planto_project.dto.RolesOfUserDto;
import planto_project.dto.UpdateUserDto;
import planto_project.dto.UserDto;
import planto_project.dto.UserRegisterDto;
import planto_project.service.UserService;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class UserController {
    final UserService userService;

    @PostMapping("/register")
    public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
        return userService.register(userRegisterDto);
    }

   @GetMapping("user/{login}")
    public UserDto findUserByLogin(@PathVariable String login) {
        return userService.findUserByLogin(login);
    }

    @PutMapping("user/{login}")
    public UserDto updateUser(@PathVariable String login, @RequestBody UpdateUserDto updateUserDto) {
        return userService.updateUser(login, updateUserDto);
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

    @PostMapping("/login")
    public UserDto login(Principal principal) {
        return userService.getUser(principal.getName());
    }
}
