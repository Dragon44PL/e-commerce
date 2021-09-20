package org.ecommerce.shop.products.stock.exception;

public class ProductInactiveException extends ProductUnavailableException {

    public ProductInactiveException() {
        super();
    }

    public ProductInactiveException(String message) {
        super(message);
    }
}
