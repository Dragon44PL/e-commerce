package accounting.authority.events;

import java.time.Instant;
import java.util.UUID;

public record AuthorityCreatedEvent(Instant occurredOn, UUID aggregateId, String name) implements AuthorityEvent {

    public AuthorityCreatedEvent(UUID aggregateId, String name) {
        this(Instant.now(), aggregateId, name);
    }
}
