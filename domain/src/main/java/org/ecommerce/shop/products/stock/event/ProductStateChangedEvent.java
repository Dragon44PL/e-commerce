package org.ecommerce.shop.products.stock.event;

import org.ecommerce.shop.products.stock.vo.ProductState;
import java.time.Instant;
import java.util.UUID;

public record ProductStateChangedEvent(Instant occurredOn, UUID aggregateId, ProductState productState) implements ProductStockEvent {

    public ProductStateChangedEvent(UUID productStockId, ProductState productState) {
        this(Instant.now(), productStockId, productState);
    }
}
