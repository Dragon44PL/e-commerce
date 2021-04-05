package accounting.role.events;

import domain.events.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public class RoleEvent implements DomainEvent<UUID> {
    private final Instant instant;
    private final UUID roleId;

    public RoleEvent(UUID roleId) {
        this.instant = Instant.now();
        this.roleId = roleId;
    }

    @Override
    public UUID aggregateId() {
        return roleId;
    }

    public Instant onInstant() {
        return instant;
    }
}
