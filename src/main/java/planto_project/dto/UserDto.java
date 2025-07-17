package planto_project.dto;

import org.jetbrains.annotations.Nullable;
import lombok.*;
import planto_project.model.Role;

import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String login;
    private String firstName;
    private String lastName;
    private String email;
    @Nullable
    private AddressDto address;
    @Singular
    private Set<Role> roles;
    @Setter
    private List<OrderResponseDto> orders;
    private Set<CartItemDto> cart;
}
