package products.stock.event;

import products.stock.vo.ProductQuantity;
import java.time.Instant;
import java.util.UUID;

public record QuantityChangedEvent(Instant occurredOn, UUID aggregateId, ProductQuantity productQuantity) implements ProductStockEvent {

    public QuantityChangedEvent(UUID productStockId, ProductQuantity productQuantity) {
        this(Instant.now(), productStockId, productQuantity);
    }
}
