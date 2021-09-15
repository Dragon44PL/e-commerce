package org.ecommerce.shop.accounting.account.events;

import org.ecommerce.shop.accounting.account.vo.Password;
import java.time.Instant;
import java.util.UUID;

public record PasswordChangedEvent(Instant occurredOn, UUID aggregateId, Password password) implements AccountEvent {

    public PasswordChangedEvent(UUID accountId, Password password) {
        this(Instant.now(), accountId, password);
    }
}
