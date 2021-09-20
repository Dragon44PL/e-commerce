package org.ecommerce.shop.products.stock.exception;

public class ProductClosedException extends ProductUnavailableException {

    public ProductClosedException() {
        super();
    }

    public ProductClosedException(String message) {
        super(message);
    }
}
