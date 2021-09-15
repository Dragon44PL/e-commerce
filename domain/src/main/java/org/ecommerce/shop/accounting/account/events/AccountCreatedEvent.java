package org.ecommerce.shop.accounting.account.events;

import org.ecommerce.shop.accounting.account.vo.Credentials;
import org.ecommerce.shop.accounting.account.vo.RoleId;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record AccountCreatedEvent(Instant occurredOn, UUID aggregateId, Credentials credentials, String mail, Set<RoleId> roles) implements AccountEvent {

    public AccountCreatedEvent(UUID aggregateId, Credentials credentials, String mail, Set<RoleId> roles) {
        this(Instant.now(), aggregateId, credentials, mail, roles);
    }

}
