package planto_project.dto;
import java.util.List;
import java.util.Set;
import lombok.*;
import planto_project.model.Role;
import planto_project.dto.AddressDto;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private AddressDto address;
    @Singular
    private Set<Role> roles;
    @Setter
    private List<OrderResponseDto> orders;
    private Set<CartItemDto> cart;
}
