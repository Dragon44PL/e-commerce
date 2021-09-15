package org.ecommerce.shop.accounting.account.events;

import java.time.Instant;
import java.util.UUID;

public record UsernameChangedEvent(Instant occurredOn, UUID aggregateId, String username) implements AccountEvent {

    public UsernameChangedEvent(UUID accountId, String username) {
        this(Instant.now(), accountId, username);
    }
}
