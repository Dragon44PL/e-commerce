package users.events.role;

import users.snapshot.RoleSnapshot;
import java.time.Instant;

public class RoleEvent {
    private final Instant instant;
    private final RoleSnapshot role;

    public RoleEvent(RoleSnapshot role) {
        this.instant = Instant.now();
        this.role = role;
    }

    public static RoleEvent ofRoleSnapshot(RoleSnapshot roleSnapshot) {
        return new RoleEvent(roleSnapshot);
    }

    public Instant onInstant() {
        return instant;
    }

    public RoleSnapshot getRole() {
        return role;
    }
}
