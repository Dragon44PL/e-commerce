package users.exception;

public class CredentialsExpiredException extends RuntimeException {

    public CredentialsExpiredException() {
        super();
    }

    public CredentialsExpiredException(String message) {
        super(message);
    }

}
