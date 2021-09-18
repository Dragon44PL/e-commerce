package org.ecommerce.shop.order;

import org.ecommerce.shop.finance.vo.Money;
import org.ecommerce.shop.order.event.OrderCreatedEvent;
import org.ecommerce.shop.order.event.OrderEvent;
import org.ecommerce.shop.order.event.TotalPriceCalculatedEvent;
import org.ecommerce.shop.order.vo.OrderDestination;
import org.ecommerce.shop.order.vo.OrderStatus;
import org.ecommerce.shop.order.vo.OrderedProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private final UUID ORDER_ID = UUID.randomUUID();
    private final OrderedProduct ORDERED_PRODUCT = OrderedProduct.create(UUID.randomUUID(), new Money(5, Money.DEFAULT_CURRENCY));
    private final OrderDestination ORDER_DESTINATION = new OrderDestination(UUID.randomUUID());
    private final OrderStatus ORDER_STATUS = OrderStatus.PENDING;

    // OrderEvents
    private final Class<OrderCreatedEvent> ORDER_CREATED_EVENT = OrderCreatedEvent.class;
    private final Class<TotalPriceCalculatedEvent> TOTAL_PRICE_CALCULATED_EVENT = TotalPriceCalculatedEvent.class;

    @Test
    @DisplayName("Order Creation Should Generate OrderCreatedEvent properly")
    void orderCreationShouldCreateProperly() {
        final Set<OrderedProduct> orderedProducts = Set.of(ORDERED_PRODUCT);
        final Order order = Order.create(ORDER_ID, orderedProducts, ORDER_DESTINATION);

        final Optional<OrderEvent> orderEvent = order.findLatestEvent();
        assertTrue(orderEvent.isPresent());
        assertEquals(ORDER_CREATED_EVENT, orderEvent.get().getClass());

        final OrderCreatedEvent orderCreatedEvent = (OrderCreatedEvent) orderEvent.get();
        assertEquals(orderCreatedEvent.aggregateId(), ORDER_ID);
        assertEquals(orderCreatedEvent.products(), orderedProducts);
        assertEquals(orderCreatedEvent.destination(), ORDER_DESTINATION);
        assertEquals(orderCreatedEvent.orderStatus(), OrderStatus.PENDING);
    }

    @Test
    @DisplayName("Order Restore Should Not Generate ProductCreatedEvent")
    void orderRestoreShouldNotGenerateEvent() {
        final Set<OrderedProduct> orderedProducts = Set.of(ORDERED_PRODUCT);
        final Order order = Order.restore(ORDER_ID, orderedProducts, ORDER_STATUS, ORDER_DESTINATION);

        final Optional<OrderEvent> orderEvent = order.findLatestEvent();
        assertFalse(orderEvent.isPresent());
    }

    @Test
    @DisplayName("Order Should Calculate Total Cost Correctly When One Product")
    void orderShouldCalculateTotalCostCorrectlyWhenOneProduct() {
        final Set<OrderedProduct> orderedProducts = Set.of(ORDERED_PRODUCT);
        final Order order = Order.restore(ORDER_ID, orderedProducts, ORDER_STATUS, ORDER_DESTINATION);
        order.calculateTotalCost();

        final Optional<OrderEvent> orderEvent = order.findLatestEvent();
        assertTrue(orderEvent.isPresent());
        assertEquals(TOTAL_PRICE_CALCULATED_EVENT, orderEvent.get().getClass());

        final TotalPriceCalculatedEvent totalPriceCalculatedEvent = (TotalPriceCalculatedEvent) orderEvent.get();
        assertEquals(totalPriceCalculatedEvent.aggregateId(), ORDER_ID);
        assertEquals(totalPriceCalculatedEvent.price(), ORDERED_PRODUCT.calculateTotalPrice());
    }

    @Test
    @DisplayName("Order Should Calculate Total Cost Correctly When One Product")
    void orderShouldCalculateTotalCostCorrectlyWhenMultipleProduct() {
        final OrderedProduct firstProduct = new OrderedProduct(UUID.randomUUID(), new Money(7, Money.DEFAULT_CURRENCY), 5);
        final OrderedProduct secondProduct = new OrderedProduct(UUID.randomUUID(), new Money(10, Money.DEFAULT_CURRENCY), 13);

        final Set<OrderedProduct> orderedProducts = Set.of(firstProduct, secondProduct);
        final Order order = Order.restore(ORDER_ID, orderedProducts, ORDER_STATUS, ORDER_DESTINATION);
        order.calculateTotalCost();

        final Optional<OrderEvent> orderEvent = order.findLatestEvent();
        assertTrue(orderEvent.isPresent());
        assertEquals(TOTAL_PRICE_CALCULATED_EVENT, orderEvent.get().getClass());

        final TotalPriceCalculatedEvent totalPriceCalculatedEvent = (TotalPriceCalculatedEvent) orderEvent.get();
        assertEquals(totalPriceCalculatedEvent.aggregateId(), ORDER_ID);
        assertEquals(totalPriceCalculatedEvent.price(), firstProduct.calculateTotalPrice().add(secondProduct.calculateTotalPrice()));
    }

    @Test
    @DisplayName("Order Should Calculate Zero When No Product")
    void orderShouldCalculateZeroWhenNoProduct() {
        final Order order = Order.restore(ORDER_ID, new HashSet<>(), ORDER_STATUS, ORDER_DESTINATION);
        order.calculateTotalCost();

        final Optional<OrderEvent> orderEvent = order.findLatestEvent();
        assertTrue(orderEvent.isPresent());
        assertEquals(TOTAL_PRICE_CALCULATED_EVENT, orderEvent.get().getClass());

        final TotalPriceCalculatedEvent totalPriceCalculatedEvent = (TotalPriceCalculatedEvent) orderEvent.get();
        assertEquals(totalPriceCalculatedEvent.aggregateId(), ORDER_ID);
        assertEquals(totalPriceCalculatedEvent.price(), Money.ZERO);
    }
}
