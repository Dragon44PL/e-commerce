package org.ecommerce.shop.order;

import org.ecommerce.shop.core.AggregateRoot;
import org.ecommerce.shop.finance.vo.Money;
import org.ecommerce.shop.order.event.*;
import org.ecommerce.shop.order.exception.OrderStatusNotCapableException;
import org.ecommerce.shop.order.vo.OrderDestination;
import org.ecommerce.shop.order.vo.OrderStatus;
import org.ecommerce.shop.order.vo.OrderedProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

class Order extends AggregateRoot<UUID, OrderEvent> {

    private final UUID id;
    private final Set<OrderedProduct> products;
    private OrderStatus orderStatus;
    private OrderDestination orderDestination;

    static Order create(UUID id, Set<OrderedProduct> products, OrderDestination orderDestination) {
        final Order order = new Order(id, products, orderDestination, new ArrayList<>());
        order.registerEvent(new OrderCreatedEvent(order.id, order.products, order.orderDestination, order.orderStatus));
        return order;
    }

    static Order restore(UUID id, Set<OrderedProduct> products, OrderStatus orderStatus, OrderDestination orderDestination) {
        return new Order(id, products, orderStatus, orderDestination, new ArrayList<>());
    }

    private Order(UUID id, Set<OrderedProduct> products, OrderDestination orderDestination, List<OrderEvent> events) {
        this(id, products, OrderStatus.PENDING, orderDestination, events);
    }

    private Order(UUID id, Set<OrderedProduct> products, OrderStatus orderStatus, OrderDestination orderDestination, List<OrderEvent> events) {
        super(events);
        this.id = id;
        this.products = products;
        this.orderStatus = orderStatus;
        this.orderDestination = orderDestination;
    }

    void calculateTotalCost() {
        final Money totalCost = products.stream().map(OrderedProduct::calculateTotalPrice).reduce(Money.ZERO, Money::add);
        final TotalPriceCalculatedEvent totalPriceCalculatedEvent = new TotalPriceCalculatedEvent(id, totalCost);
        this.registerEvent(totalPriceCalculatedEvent);
    }

    void changeOrderDestination(OrderDestination orderDestination) throws OrderStatusNotCapableException {
        checkOrderStatus();
        if(!containsDestination(orderDestination)) {
            this.orderDestination = orderDestination;
            final OrderDestinationChangedEvent orderDestinationChangedEvent = new OrderDestinationChangedEvent(this.id, orderDestination);
            this.registerEvent(orderDestinationChangedEvent);
        }
    }

    private void checkOrderStatus() {
        if(!this.orderStatus.isCapableToProcess()) {
            throw new OrderStatusNotCapableException(orderStatus);
        }
    }

    private boolean containsDestination(OrderDestination orderDestination) {
        return this.orderDestination.equals(orderDestination);
    }
}
