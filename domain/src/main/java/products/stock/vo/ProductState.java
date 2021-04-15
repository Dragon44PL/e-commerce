package products.stock.vo;

public enum ProductState {

    AVAILABLE, ACTIVE, OUT_OF_STOCK, INACTIVE, CLOSED;

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isInactive() {
        return this == INACTIVE;
    }

    public boolean isOutOfStock() {
        return this == OUT_OF_STOCK;
    }

    public boolean isClosed() {
        return this == CLOSED;
    }
}
