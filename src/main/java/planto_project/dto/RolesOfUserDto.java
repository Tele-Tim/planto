package planto_project.dto;

import lombok.*;
import planto_project.model.Role;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RolesOfUserDto {
    String login;
    @Singular
    Set<Role> roles;
}
