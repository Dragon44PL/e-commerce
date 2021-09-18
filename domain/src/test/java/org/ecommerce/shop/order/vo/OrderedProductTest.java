package org.ecommerce.shop.order.vo;

import org.ecommerce.shop.finance.vo.Money;
import org.ecommerce.shop.order.exception.NegativeAmountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Currency;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderedProductTest {

    private final int PRODUCT_AMOUNT = 10;
    private final Currency PRODUCT_CURRENCY = Money.DEFAULT_CURRENCY;
    private final Money PRODUCT_PRICE = new Money(10, PRODUCT_CURRENCY);
    private final UUID PRODUCT_ID = UUID.randomUUID();

    private final int POSITIVE_VALUE = PRODUCT_AMOUNT - 5;
    private final int NEGATIVE_VALUE = -(POSITIVE_VALUE);

    private final int GREATER_POSITIVE_VALUE = POSITIVE_VALUE * 2;
    private final int GREATER_NEGATIVE_VALUE = NEGATIVE_VALUE * 2;

    // OrderedProduct Exceptions
    private final Class<NegativeAmountException> NEGATIVE_AMOUNT_EXCEPTION = NegativeAmountException.class;

    @Test
    @DisplayName("Ordered Product Should Be Created Properly")
    void shouldCreateOrderedProductProperly() {
        final OrderedProduct orderedProduct = new OrderedProduct(PRODUCT_ID, PRODUCT_PRICE, PRODUCT_AMOUNT);
        assertEquals(PRODUCT_ID, orderedProduct.productId());
        assertEquals(PRODUCT_PRICE, orderedProduct.price());
        assertEquals(PRODUCT_AMOUNT, orderedProduct.amount());
    }

    @Test
    @DisplayName("Ordered Product Should Calculate Total Price Properly")
    void shouldCalculateTotalPriceProperly() {
        final OrderedProduct orderedProduct = new OrderedProduct(PRODUCT_ID, PRODUCT_PRICE, PRODUCT_AMOUNT);
        assertEquals(PRODUCT_PRICE.multiply(PRODUCT_AMOUNT), orderedProduct.calculateTotalPrice());
    }

    @Test
    @DisplayName("Ordered Product Should Be Created With One Amount Properly")
    void shouldCreateWithOneAmount() {
        final OrderedProduct orderedProduct = OrderedProduct.create(PRODUCT_ID, PRODUCT_PRICE);
        assertEquals(OrderedProduct.MIN_AMOUNT, orderedProduct.amount());
    }

    @Test
    @DisplayName("Ordered Product Should Increase Amount When Positive Amount")
    void shouldIncreaseAmountWhenPositiveAmount() {
        final OrderedProduct orderedProduct = new OrderedProduct(PRODUCT_ID, PRODUCT_PRICE, PRODUCT_AMOUNT);
        assertEquals(PRODUCT_AMOUNT + POSITIVE_VALUE, orderedProduct.increaseAmount(POSITIVE_VALUE).amount());
    }

    @Test
    @DisplayName("Ordered Product Should Not Increase Amount When Negative Amount")
    void shouldIncreaseAmountWhenNegativeAmount() {
        final OrderedProduct orderedProduct = new OrderedProduct(PRODUCT_ID, PRODUCT_PRICE, PRODUCT_AMOUNT);
        assertEquals(PRODUCT_AMOUNT, orderedProduct.increaseAmount(NEGATIVE_VALUE).amount());
    }

    @Test
    @DisplayName("Ordered Product Should Decrease Amount When Positive Amount")
    void shouldDecreaseAmountWhenPositiveAmount() {
        final OrderedProduct orderedProduct = new OrderedProduct(PRODUCT_ID, PRODUCT_PRICE, PRODUCT_AMOUNT);
        assertEquals(PRODUCT_AMOUNT - POSITIVE_VALUE, orderedProduct.decreaseAmount(POSITIVE_VALUE).amount());
    }

    @Test
    @DisplayName("Ordered Product Should Not Decrease Amount When Negative Amount")
    void shouldDecreaseAmountWhenNegativeAmount() {
        final OrderedProduct orderedProduct = new OrderedProduct(PRODUCT_ID, PRODUCT_PRICE, PRODUCT_AMOUNT);
        assertEquals(PRODUCT_AMOUNT, orderedProduct.decreaseAmount(NEGATIVE_VALUE).amount());
    }

    @Test
    @DisplayName("Ordered Product Should Change Amount When Positive Amount")
    void shouldChangeAmountWhenPositiveAmount() {
        final OrderedProduct orderedProduct = new OrderedProduct(PRODUCT_ID, PRODUCT_PRICE, PRODUCT_AMOUNT);
        assertEquals(PRODUCT_AMOUNT + POSITIVE_VALUE, orderedProduct.changeAmount(POSITIVE_VALUE).amount());
    }

    @Test
    @DisplayName("Ordered Product Should Change Amount When Negative Amount")
    void shouldChangeAmountWhenNegativeAmount() {
        final OrderedProduct orderedProduct = new OrderedProduct(PRODUCT_ID, PRODUCT_PRICE, PRODUCT_AMOUNT);
        assertEquals(PRODUCT_AMOUNT - (-NEGATIVE_VALUE), orderedProduct.changeAmount(NEGATIVE_VALUE).amount());
    }

    @Test
    @DisplayName("Ordered Product Should Throw NegativeAmountException When Amount Is Non Positive When Decreasing")
    void shouldThrowNegativeAmountExceptionWhenDecreasing() {
        final OrderedProduct orderedProduct = new OrderedProduct(PRODUCT_ID, PRODUCT_PRICE, PRODUCT_AMOUNT);
        assertThrows(NEGATIVE_AMOUNT_EXCEPTION, () -> orderedProduct.decreaseAmount(GREATER_POSITIVE_VALUE));
    }

    @Test
    @DisplayName("Ordered Product Should Throw NegativeAmountException When Amount Is Non Positive And Changing")
    void shouldThrowNegativeAmountExceptionWhenChanging() {
        final OrderedProduct orderedProduct = new OrderedProduct(PRODUCT_ID, PRODUCT_PRICE, PRODUCT_AMOUNT);
        assertThrows(NEGATIVE_AMOUNT_EXCEPTION, () -> orderedProduct.changeAmount(GREATER_NEGATIVE_VALUE));
    }

    @Test
    @DisplayName("couldAmountChange Should Return True When Giving Positive Amount")
    void shouldReturnTrueWhenPositiveAmount() {
        final OrderedProduct orderedProduct = new OrderedProduct(PRODUCT_ID, PRODUCT_PRICE, PRODUCT_AMOUNT);
        assertTrue(orderedProduct.couldAmountChange(PRODUCT_AMOUNT - 5));
    }

    @Test
    @DisplayName("couldAmountChange Should Return True When Giving Non Positive Amount")
    void shouldReturnFalseWhenNotPositiveAmount() {
        final OrderedProduct orderedProduct = new OrderedProduct(PRODUCT_ID, PRODUCT_PRICE, PRODUCT_AMOUNT);
        assertFalse(orderedProduct.couldAmountChange(GREATER_POSITIVE_VALUE));
    }
}