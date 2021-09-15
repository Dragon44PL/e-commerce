package org.ecommerce.shop.accounting.account.events;

import org.ecommerce.shop.accounting.account.vo.RoleId;
import java.time.Instant;
import java.util.UUID;

public record RoleRemovedEvent(Instant occurredOn, UUID aggregateId, RoleId role) implements AccountEvent {

    public RoleRemovedEvent(UUID accountId, RoleId role) {
        this(Instant.now(), accountId, role);
    }
}
