package products.stock.event;

import products.stock.vo.ProductAvailability;
import products.stock.vo.ProductState;
import java.time.Instant;
import java.util.UUID;

public record ProductAvailabilityChangedEvent(Instant occurredOn, UUID aggregateId, ProductAvailability productAvailability, ProductState productState) implements ProductStockEvent {

    public ProductAvailabilityChangedEvent(UUID productStockId, ProductAvailability productAvailability, ProductState productState) {
        this(Instant.now(), productStockId, productAvailability, productState);
    }
}
