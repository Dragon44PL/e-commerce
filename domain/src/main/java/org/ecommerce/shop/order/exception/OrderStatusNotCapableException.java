package org.ecommerce.shop.order.exception;

import org.ecommerce.shop.order.vo.OrderStatus;

public class OrderStatusNotCapableException extends RuntimeException {

    private final OrderStatus orderStatus;

    public OrderStatusNotCapableException(OrderStatus orderStatus) {
        super();
        this.orderStatus = orderStatus;
    }

    public OrderStatusNotCapableException(String message, OrderStatus orderStatus) {
        super(message);
        this.orderStatus = orderStatus;
    }

    public OrderStatus orderStatus() {
        return orderStatus;
    }
}
