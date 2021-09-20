package org.ecommerce.shop.products.stock.exception;

public class ProductOutOfStockException extends ProductUnavailableException {

    public ProductOutOfStockException() {
        super();
    }

    public ProductOutOfStockException(String message) {
        super(message);
    }
}
