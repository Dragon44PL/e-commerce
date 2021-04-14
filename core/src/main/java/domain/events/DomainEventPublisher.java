package domain.events;

public interface DomainEventPublisher {
    void publish(DomainEvent t);
}
