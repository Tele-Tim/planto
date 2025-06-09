package planto_project.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
@Document(collection = "users")
public class UserAccount {
    @Id
    String login;
    @Setter
    String firstName;
    @Setter
    String lastName;
    @Setter
    String email;
    @Setter
    String password;
    Set<Role> roles;

    public UserAccount() {
        roles = new HashSet<>();
        roles.add(Role.USER);
    }

    public UserAccount(String login, String firstName, String lastName, String email, String password) {
        this();
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public boolean addRole(String role) {
        return roles.add(Role.valueOf(role.toUpperCase()));
    }

    public boolean removeRole(String role) {
        return roles.remove(Role.valueOf(role.toUpperCase()));
    }
}
