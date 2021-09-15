package org.ecommerce.shop.accounting.account.events;

import java.time.Instant;
import java.util.UUID;

public record MailChangedEvent(Instant occurredOn, UUID aggregateId, String mail) implements AccountEvent {

    public MailChangedEvent(UUID accountId, String mail) {
        this(Instant.now(), accountId, mail);
    }
}
