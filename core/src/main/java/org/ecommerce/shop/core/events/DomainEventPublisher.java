package org.ecommerce.shop.core.events;

public interface DomainEventPublisher {
    void publish(DomainEvent t);
}
