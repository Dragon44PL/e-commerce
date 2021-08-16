package finance.exception;

public class NegativeTaxRatioException extends RuntimeException {

    public NegativeTaxRatioException() {
        super();
    }

    public NegativeTaxRatioException(String message) {
        super(message);
    }
}
