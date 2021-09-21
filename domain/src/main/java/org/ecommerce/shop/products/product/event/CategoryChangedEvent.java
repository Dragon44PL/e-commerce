package org.ecommerce.shop.products.product.event;

import org.ecommerce.shop.products.product.vo.CategoryId;
import java.time.Instant;
import java.util.UUID;

public record CategoryChangedEvent(Instant occurredOn, UUID aggregateId, CategoryId category) implements ProductEvent {

    public CategoryChangedEvent(UUID productId, CategoryId category) {
        this(Instant.now(), productId, category);
    }
}
