package planto_project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRegisterDto {
    String login;
    String firstName;
    String lastName;
    @NotBlank
    @Email
    String email;
    @NotBlank
    String password;
}
