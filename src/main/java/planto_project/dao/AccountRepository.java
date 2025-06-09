package planto_project.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import planto_project.dto.UserDto;
import planto_project.model.Role;
import planto_project.model.UserAccount;

import java.util.Set;


public interface AccountRepository extends MongoRepository<UserAccount, String> {
    UserAccount findUserByLogin(String login);

    boolean existsByRolesContaining(Set<Role> roles);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    UserDto getUserAccountByLogin(String login);
}
