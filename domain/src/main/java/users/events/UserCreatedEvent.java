package users.events;

import java.time.Instant;
import java.util.UUID;

public record UserCreatedEvent(Instant occurredOn, UUID aggregateId) implements UserEvent {
    public UserCreatedEvent(UUID userId) {
        this(Instant.now(), userId);
    }
}
