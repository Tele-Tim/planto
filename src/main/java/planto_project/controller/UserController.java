package planto_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import planto_project.dto.*;
import planto_project.security.JwtUtil;
import planto_project.service.UserService;

import java.security.Principal;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class UserController {
    final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody UserRegisterDto userRegisterDto) {
        userService.register(userRegisterDto);
        String token = jwtUtil.generateJwtToken(userRegisterDto.getLogin());
        return ResponseEntity.ok(new AuthResponseDto(token));
    }

    @GetMapping("user/{login}")
    public UserDto findUserByLogin(@PathVariable String login) {
        return userService.findUserByLogin(login);
    }

    @GetMapping("users")
    public Set<UserDto> findAllUsers() {
        return userService.findAllUsers();
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
}
