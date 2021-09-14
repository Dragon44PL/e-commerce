package org.ecommerce.shop.users.location.event;

import org.ecommerce.shop.users.location.vo.Address;
import java.time.Instant;
import java.util.UUID;

public record AddressChangedEvent(Instant occurredOn, UUID aggregateId, Address address) implements LocationEvent {

    public AddressChangedEvent(UUID locationId, Address address) {
        this(Instant.now(), locationId, address);
    }
}
