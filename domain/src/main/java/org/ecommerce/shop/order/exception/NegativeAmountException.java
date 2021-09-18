package org.ecommerce.shop.order.exception;

public class NegativeAmountException extends RuntimeException {

    public NegativeAmountException() {
        super();
    }

    public NegativeAmountException(String message) {
        super(message);
    }
}
