package planto_project.service;

import planto_project.dto.RolesOfUserDto;
import planto_project.dto.UpdateUserDto;
import planto_project.dto.UserDto;
import planto_project.dto.UserRegisterDto;


public interface UserService {
    UserDto register(UserRegisterDto userRegisterDto);

    UserDto updateUser(String login, UpdateUserDto updateUserDto);

    UserDto removeUser(String login);

    UserDto findUserByLogin(String login);

    void changePassword(String login, String newPassword);

    RolesOfUserDto changeRolesList(String login, String role, boolean b);

    UserDto getUser(String login);
}
