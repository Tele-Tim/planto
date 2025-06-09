package planto_project.exceptions;

public class LoginAlreadyExistException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Login already exists";

    public LoginAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

    public LoginAlreadyExistException(String login) {
        super("User with login " + login + " already exists");
    }
}

