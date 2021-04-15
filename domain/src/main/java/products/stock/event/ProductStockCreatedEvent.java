package products.stock.event;

import products.stock.vo.ProductId;
import products.stock.vo.ProductQuantity;
import products.stock.vo.ProductState;
import java.time.Instant;
import java.util.UUID;

public record ProductStockCreatedEvent(Instant occurredOn, UUID aggregateId, ProductId product, ProductState productState, ProductQuantity quantity) implements ProductStockEvent {

    public ProductStockCreatedEvent(UUID productStockId, ProductId product, ProductState productState, ProductQuantity quantity) {
        this(Instant.now(), productStockId, product, productState, quantity);
    }
}
