package org.ecommerce.shop.order.event;

import org.ecommerce.shop.order.vo.OrderStatus;

import java.time.Instant;
import java.util.UUID;

public record OrderStatusChangedEvent(Instant occurredOn, UUID aggregateId, OrderStatus orderStatus) implements OrderEvent {

    public OrderStatusChangedEvent(UUID orderId, OrderStatus orderStatus) {
        this(Instant.now(), orderId, orderStatus);
    }
}
