package org.ecommerce.shop.products.product.exception;

public class NegativePriceException extends RuntimeException {

    public NegativePriceException() {
        super();
    }

    public NegativePriceException(String message) {
        super(message);
    }
}
