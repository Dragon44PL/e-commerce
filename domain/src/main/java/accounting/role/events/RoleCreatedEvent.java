package accounting.role.events;

import accounting.role.vo.AuthorityId;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record RoleCreatedEvent(Instant occurredOn, UUID aggregateId, String name, Set<AuthorityId> authorities) implements RoleEvent {

    public RoleCreatedEvent(UUID aggregateId, String name, Set<AuthorityId> authorities) {
        this(Instant.now(), aggregateId, name, authorities);
    }
}
