package planto_project.exceptions;

public class InvalidLoginOfUserException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Login is not valid";

    public InvalidLoginOfUserException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidLoginOfUserException(String login) {
        super("The login " + login + " is not valid");
    }
}