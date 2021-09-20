package org.ecommerce.shop.products.stock.vo;

public enum ProductAvailability {

    AVAILABLE, OUT_OF_STOCK;

    public boolean isAvailable() {
        return this == AVAILABLE;
    }

    public boolean isOutOfStock() {
        return this == OUT_OF_STOCK;
    }
}
