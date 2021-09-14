package org.ecommerce.shop.users.location.event;

import org.ecommerce.shop.users.location.vo.Address;
import java.time.Instant;
import java.util.UUID;

public record LocationCreatedEvent(Instant occurredOn, UUID aggregateId, Address address) implements LocationEvent {

    public LocationCreatedEvent(UUID locationId, Address address) {
        this(Instant.now(), locationId, address);
    }
}
