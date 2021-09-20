package org.ecommerce.shop.products.stock.exception;

public class ProductUnavailableException extends RuntimeException {

    public ProductUnavailableException() {
        super();
    }

    public ProductUnavailableException(String message) {
        super(message);
    }
}
