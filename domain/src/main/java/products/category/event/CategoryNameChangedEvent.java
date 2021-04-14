package products.category.event;

import java.time.Instant;
import java.util.UUID;

public record CategoryNameChangedEvent(Instant occurredOn, UUID aggregateId, String name) implements CategoryEvent {

    public CategoryNameChangedEvent(UUID categoryId, String name) {
        this(Instant.now(), categoryId, name);
    }
}
