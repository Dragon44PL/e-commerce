package products.category.event;

import products.category.vo.CategoryId;
import java.time.Instant;
import java.util.UUID;

public record CategoryCreatedEvent(Instant occurredOn, UUID aggregateId, String name, CategoryId parentCategory) implements CategoryEvent {

    public CategoryCreatedEvent(UUID categoryId, String name, CategoryId parentCategory) {
        this(Instant.now(), categoryId, name, parentCategory);
    }
}
