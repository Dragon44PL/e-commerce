package org.ecommerce.shop.products.stock.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ecommerce.shop.products.stock.exception.ProductOutOfStockException;

import static org.junit.jupiter.api.Assertions.*;

class ProductQuantityTest {

    private final int QUANTITY = 10;
    private final int ZERO_QUANTITY = 0;
    private final int NEGATIVE_QUANTITY = -10;

    private final int INCREASE = 5;
    private final int DECREASE = 10;

    private final Class<ProductOutOfStockException> PRODUCT_OUT_OF_STOCK_EXCEPTION = ProductOutOfStockException.class;

    @Test
    @DisplayName("Product Quantity Should Throw ProductOutOfStockException")
    void productQuantityShouldCreateWhenPositiveQuantity() {
        final ProductQuantity productQuantity = new ProductQuantity(QUANTITY);
    }

    @Test
    @DisplayName("Product Quantity Should Throw ProductOutOfStockException")
    void productQuantityShouldCreateWhenZeroQuantity() {
        final ProductQuantity productQuantity = new ProductQuantity(ZERO_QUANTITY);
    }

    @Test
    @DisplayName("Product Quantity Should Throw ProductOutOfStockException")
    void productQuantityShouldThrowProductOutOfStockException() {
        assertThrows(PRODUCT_OUT_OF_STOCK_EXCEPTION, () -> new ProductQuantity(NEGATIVE_QUANTITY));
    }

    @Test
    @DisplayName("Product Quantity Should Be Increased")
    void productQuantityShouldIncrease() {
        final ProductQuantity productQuantity = new ProductQuantity(QUANTITY);
        final ProductQuantity increased = productQuantity.increaseQuantity(INCREASE);
        assertEquals(increased.quantity(), INCREASE + QUANTITY);
    }

    @Test
    @DisplayName("Product Quantity Should Be Decreased")
    void productQuantityShouldDecrease() {
        final ProductQuantity productQuantity = new ProductQuantity(QUANTITY);
        final ProductQuantity decreased = productQuantity.decreaseQuantity(DECREASE);
        assertEquals(QUANTITY - DECREASE, decreased.quantity());
    }

    @Test
    @DisplayName("Product Quantity Should Not Be Decreased And Throw ProductOutOfStockException")
    void productQuantityShouldNotDecreaseAndThrowProductOutOfStockException() {
        final ProductQuantity productQuantity = new ProductQuantity(QUANTITY);
        final ProductQuantity decreased = productQuantity.decreaseQuantity(DECREASE);
        assertThrows(PRODUCT_OUT_OF_STOCK_EXCEPTION, () -> decreased.decreaseQuantity(DECREASE));
    }

    @Test
    @DisplayName("Product Quantity Should Be Decreased When Increasing With Negative Value")
    void productQuantityShouldDecreaseWhenIncreasingWithNegativeValue() {
        final ProductQuantity productQuantity = new ProductQuantity(QUANTITY);
        final ProductQuantity increased = productQuantity.increaseQuantity(NEGATIVE_QUANTITY);
        assertEquals( QUANTITY + NEGATIVE_QUANTITY, increased.quantity());
    }

    @Test
    @DisplayName("Product Quantity Should Be Increased When Decreasing With Negative Value")
    void productQuantityShouldIncreaseWhenDecreasingWithNegativeValue() {
        final ProductQuantity productQuantity = new ProductQuantity(QUANTITY);
        final ProductQuantity decreased = productQuantity.decreaseQuantity(NEGATIVE_QUANTITY);
        assertEquals(QUANTITY - NEGATIVE_QUANTITY, decreased.quantity());
    }

    @Test
    @DisplayName("Product Quantity Should Not Be Empty")
    void productQuantityShouldNotBeEmpty() {
        final ProductQuantity productQuantity = new ProductQuantity(QUANTITY);
        assertFalse(productQuantity.isEmpty());
    }

    @Test
    @DisplayName("Product Quantity Should Be Empty")
    void productQuantityShouldBeEmpty() {
        final ProductQuantity productQuantity = new ProductQuantity(ZERO_QUANTITY);
        assertTrue(productQuantity.isEmpty());
    }

    @Test
    @DisplayName("Positive Product Quantity Should Be Enough")
    void positiveProductQuantityShouldBeEnough() {
        assertTrue(ProductQuantity.isQuantityEnough(QUANTITY));
    }

    @Test
    @DisplayName("Zero Product Quantity Should Be Enough")
    void zeroProductQuantityShouldBeEnough() {
        assertTrue(ProductQuantity.isQuantityEnough(ZERO_QUANTITY));
    }

    @Test
    @DisplayName("Negative Product Quantity Should Not Be Enough")
    void negativeProductQuantityShouldNotBeEnough() {
        assertFalse(ProductQuantity.isQuantityEnough(NEGATIVE_QUANTITY));
    }

}
