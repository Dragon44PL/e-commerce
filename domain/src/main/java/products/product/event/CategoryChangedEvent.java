package products.product.event;

import products.product.vo.CategoryId;
import java.time.Instant;
import java.util.UUID;

public record CategoryChangedEvent(Instant occurredOn, UUID aggregateId, CategoryId category) implements ProductEvent {

    public CategoryChangedEvent(UUID productId, CategoryId category) {
        this(Instant.now(), productId, category);
    }
}
