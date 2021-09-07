package org.ecommerce.shop.core.events;

import java.time.Instant;

public interface DomainEvent<I> {
    I aggregateId();
    Instant occurredOn();
}
