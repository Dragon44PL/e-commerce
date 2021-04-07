package accounting.role.events;

import accounting.role.vo.AuthorityId;
import java.time.Instant;
import java.util.UUID;

public record AuthorityAddedEvent(Instant occurredOn, UUID aggregateId, AuthorityId authority) implements RoleEvent {

    public AuthorityAddedEvent(UUID roleId, AuthorityId authority) {
        this(Instant.now(), roleId, authority);
    }
}
