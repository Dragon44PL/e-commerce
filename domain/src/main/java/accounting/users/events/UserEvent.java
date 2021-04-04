package accounting.users.events;

import domain.events.DomainEvent;
import accounting.users.UserSnapshot;

import java.time.Instant;

public class UserEvent implements DomainEvent {
    private final Instant instant;
    private final UserSnapshot user;

    public UserEvent(UserSnapshot user) {
        this.instant = Instant.now();
        this.user = user;
    }

    public static UserEvent ofUserSnapshot(UserSnapshot user) {
        return new UserEvent(user);
    }

    public Instant onInstant() {
        return instant;
    }

    public UserSnapshot getUser() {
        return user;
    }
}
