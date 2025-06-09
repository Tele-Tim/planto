package planto_project.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import planto_project.dao.AccountRepository;
import planto_project.dto.UpdateUserDto;
import planto_project.exceptions.EmailAlreadyExistException;
import planto_project.exceptions.InvalidLoginOfUserException;
import planto_project.exceptions.LoginAlreadyExistException;
import planto_project.model.UserAccount;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class UserValidator {
    final AccountRepository accountRepository;

    public void checkLogin(String login) {
        if (login == null || login.isEmpty()) {
            throw new InvalidLoginOfUserException();
        }
        if (accountRepository.existsById(login)) {
            throw new LoginAlreadyExistException(login);
        }
    }

    public void checkEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new InvalidLoginOfUserException();
        }
        if (accountRepository.existsById(email)) {
            throw new EmailAlreadyExistException(email);
        }
    }

    private <T> void updateField(T newValue, T currentValue, Consumer<T> setter) {
        if (newValue != null && !newValue.equals(currentValue)) {
            setter.accept(newValue);
        }
    }

    public void updateFields(UserAccount user, UpdateUserDto updateUserDto) {

        updateField(updateUserDto.getFirstName(), user.getFirstName(), user::setFirstName);
        updateField(updateUserDto.getLastName(), user.getLastName(), user::setLastName);
        updateField(updateUserDto.getEmail(), user.getEmail(), user::setEmail);
    }

}
