package categories.category.event;

import categories.category.vo.CategoryId;
import java.time.Instant;
import java.util.UUID;

public record ParentCategoryChangedEvent(Instant occurredOn, UUID aggregateId, CategoryId parentCategory) implements CategoryEvent {

    public ParentCategoryChangedEvent(UUID categoryId, CategoryId parentCategory) {
        this(Instant.now(), categoryId, parentCategory);
    }
}
