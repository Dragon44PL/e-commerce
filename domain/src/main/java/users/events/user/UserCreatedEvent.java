package users.events.user;

import events.DomainEvent;
import users.snapshot.UserSnapshot;
import java.time.Instant;

public class UserCreatedEvent implements DomainEvent {

    private final Instant instant;
    private final UserSnapshot user;

    public UserCreatedEvent(UserSnapshot user) {
        this.instant = Instant.now();
        this.user = user;
    }

    public Instant onInstant() {
        return instant;
    }

    public UserSnapshot getUser() {
        return user;
    }
}
