package planto_project.exceptions;

public class EmailAlreadyExistException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Email already exists";

    public EmailAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

    public EmailAlreadyExistException(String email) {
        super("User with email " + email + " already exists");
    }
}
