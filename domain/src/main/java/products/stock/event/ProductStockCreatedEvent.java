package products.stock.event;

import products.stock.vo.ProductAvailability;
import products.stock.vo.ProductId;
import products.stock.vo.ProductQuantity;
import products.stock.vo.ProductState;
import java.time.Instant;
import java.util.UUID;

public record ProductStockCreatedEvent(Instant occurredOn, UUID aggregateId, ProductId product, ProductAvailability productAvailability, ProductState productState, ProductQuantity quantity) implements ProductStockEvent {

    public ProductStockCreatedEvent(UUID productStockId, ProductId product, ProductState productState, ProductAvailability productAvailability, ProductQuantity quantity) {
        this(Instant.now(), productStockId, product, productAvailability, productState, quantity);
    }
}
