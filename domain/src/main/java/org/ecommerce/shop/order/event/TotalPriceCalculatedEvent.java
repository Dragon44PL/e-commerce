package org.ecommerce.shop.order.event;

import org.ecommerce.shop.finance.vo.Money;

import java.time.Instant;
import java.util.UUID;

public record TotalPriceCalculatedEvent(Instant occurredOn, UUID aggregateId, Money price) implements OrderEvent {

    public TotalPriceCalculatedEvent(UUID orderId, Money price) {
        this(Instant.now(), orderId, price);
    }
}
