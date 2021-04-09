package finance.exception;

public class InvalidCurrencyException extends RuntimeException {

    public InvalidCurrencyException() {
        super();
    }

    public InvalidCurrencyException(String message) {
        super(message);
    }
}
