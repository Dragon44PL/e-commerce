package org.ecommerce.shop.order.event;

import org.ecommerce.shop.order.vo.OrderDestination;

import java.time.Instant;
import java.util.UUID;

public record OrderDestinationChangedEvent(Instant occurredOn, UUID aggregateId, OrderDestination orderDestination) implements OrderEvent {

    public OrderDestinationChangedEvent(UUID orderId, OrderDestination orderDestination) {
        this(Instant.now(), orderId, orderDestination);
    }
}