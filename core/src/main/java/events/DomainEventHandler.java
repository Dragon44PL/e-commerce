package events;

public interface DomainEventHandler<T extends DomainEvent> {
    void handle(T t);
}
