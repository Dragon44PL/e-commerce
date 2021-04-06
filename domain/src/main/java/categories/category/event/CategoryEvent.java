package categories.category.event;

import domain.events.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public class CategoryEvent implements DomainEvent<UUID> {

    private final Instant instant;
    private final UUID categoryId;

    public CategoryEvent(UUID categoryId) {
        this.instant = Instant.now();
        this.categoryId = categoryId;
    }

    @Override
    public UUID aggregateId() {
        return categoryId;
    }

    @Override
    public Instant onInstant() {
        return instant;
    }
}
