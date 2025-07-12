package planto_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserDto {
    String firstName;
    String lastName;
    String email;
}
