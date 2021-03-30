package events;

import java.time.Instant;

public interface DomainEvent {
    Instant onInstant();
}
