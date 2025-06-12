package planto_project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Document(collection = "users")
@Builder
@AllArgsConstructor
public class UserAccount {
    @Id
    private String login;
    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Setter
    private String email;
    @Setter
    private String password;
    private Address address;
    private Set<Role> roles;
    private List<Order> orders;

    public UserAccount() {
        roles = new HashSet<>();
        roles.add(Role.USER);
    }

    public UserAccount(String login, String firstName,
                       String lastName, String email,
                       String password) {
        this();
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;

    }

    public boolean addRole(String role) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        return roles.add(Role.valueOf(role.toUpperCase()));
    }

    public boolean removeRole(String role) {
        if (roles == null) {
            return false;
        }
        return roles.remove(Role.valueOf(role.toUpperCase()));
    }
}
