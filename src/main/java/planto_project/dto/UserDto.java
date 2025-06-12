package planto_project.dto;

import lombok.*;
import planto_project.model.Role;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    String login;
    String firstName;
    String lastName;
    String email;
    AddressDto address;
    @Singular
    Set<Role> roles;
}
