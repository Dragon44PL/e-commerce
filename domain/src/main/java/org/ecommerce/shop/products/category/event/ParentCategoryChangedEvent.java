package org.ecommerce.shop.products.category.event;

import org.ecommerce.shop.products.category.vo.CategoryId;
import java.time.Instant;
import java.util.UUID;

public record ParentCategoryChangedEvent(Instant occurredOn, UUID aggregateId, CategoryId parentCategory) implements CategoryEvent {

    public ParentCategoryChangedEvent(UUID categoryId, CategoryId parentCategory) {
        this(Instant.now(), categoryId, parentCategory);
    }
}
