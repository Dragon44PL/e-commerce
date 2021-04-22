package products.stock.event;

import products.stock.vo.ProductAvailability;
import java.time.Instant;
import java.util.UUID;

public record ProductAvailabilityChangedEvent(Instant occurredOn, UUID aggregateId, ProductAvailability productAvailability) implements ProductStockEvent {

    public ProductAvailabilityChangedEvent(UUID productStockId, ProductAvailability productAvailability) {
        this(Instant.now(), productStockId, productAvailability);
    }
}
