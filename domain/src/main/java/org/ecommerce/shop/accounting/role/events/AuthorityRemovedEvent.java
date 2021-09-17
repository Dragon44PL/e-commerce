package org.ecommerce.shop.accounting.role.events;

import org.ecommerce.shop.accounting.role.vo.AuthorityId;
import java.time.Instant;
import java.util.UUID;

public record AuthorityRemovedEvent(Instant occurredOn, UUID aggregateId, AuthorityId authority) implements RoleEvent {

    public AuthorityRemovedEvent(UUID roleId, AuthorityId authority) {
        this(Instant.now(), roleId, authority);
    }
}
