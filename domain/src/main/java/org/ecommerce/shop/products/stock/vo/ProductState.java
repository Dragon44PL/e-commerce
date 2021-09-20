package org.ecommerce.shop.products.stock.vo;

public enum ProductState {

    ACTIVE, INACTIVE, CLOSED;

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isInactive() {
        return this == INACTIVE;
    }

    public boolean isClosed() {
        return this == CLOSED;
    }
}
