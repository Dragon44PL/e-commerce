package org.ecommerce.shop.accounting.account.exception;

public class PasswordExpiredException extends RuntimeException {

    public PasswordExpiredException() {
        super();
    }

    public PasswordExpiredException(String message) {
        super(message);
    }

}
