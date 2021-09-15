package org.ecommerce.shop.accounting.account.events;

import org.ecommerce.shop.accounting.account.vo.RoleId;
import java.time.Instant;
import java.util.UUID;

public record RoleAddedEvent(Instant occurredOn, UUID aggregateId, RoleId role) implements AccountEvent {

    public RoleAddedEvent(UUID accountId, RoleId role) {
        this(Instant.now(), accountId, role);
    }
}
