package org.ecommerce.shop.products.stock.event;

import org.ecommerce.shop.products.stock.vo.ProductAvailability;
import java.time.Instant;
import java.util.UUID;

public record ProductAvailabilityChangedEvent(Instant occurredOn, UUID aggregateId, ProductAvailability productAvailability) implements ProductStockEvent {

    public ProductAvailabilityChangedEvent(UUID productStockId, ProductAvailability productAvailability) {
        this(Instant.now(), productStockId, productAvailability);
    }
}
