package products.product.event;

import products.product.vo.ProductDetails;
import java.time.Instant;
import java.util.UUID;

public record ProductDetailsUpdatedEvent(Instant occurredOn, UUID aggregateId, ProductDetails productDetails) implements ProductEvent{

    public ProductDetailsUpdatedEvent(UUID productId, ProductDetails productDetails) {
        this(Instant.now(), productId, productDetails);
    }
}
