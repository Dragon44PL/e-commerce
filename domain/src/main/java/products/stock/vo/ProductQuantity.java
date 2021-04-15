package products.stock.vo;

import products.stock.exception.ProductOutOfStockException;

public record ProductQuantity(int quantity) {

    private static final int MIN_QUANTITY = 0;

    public ProductQuantity {
        if(!isQuantityEnough(quantity)) {
            throw new ProductOutOfStockException();
        }
    }

    public ProductQuantity increaseQuantity(int amount) throws ProductOutOfStockException {
        final int candidate = quantity + amount;
        return processCreatingProductQuantity(candidate);
    }

    public ProductQuantity decreaseQuantity(int amount) throws ProductOutOfStockException {
        final int candidate = quantity - amount;
        return processCreatingProductQuantity(candidate);
    }

    private ProductQuantity processCreatingProductQuantity(int candidate) {
        if(!isQuantityEnough(candidate)) {
            throw new ProductOutOfStockException();
        }

        return new ProductQuantity(candidate);
    }

    public boolean isQuantityEnough(int candidate) {
        return candidate >= MIN_QUANTITY;
    }

    public boolean isEmpty() {
        return quantity == MIN_QUANTITY;
    }

}
