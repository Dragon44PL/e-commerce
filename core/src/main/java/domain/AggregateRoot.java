package domain;

import domain.events.DomainEvent;
import domain.vo.EntitySnapshot;
import java.util.List;

public abstract class AggregateRoot<I , T extends EntitySnapshot, E extends DomainEvent<I>> implements Aggregate<I, T> {

    private final List<E> events;

    public AggregateRoot(List<E> events) {
        this.events = events;
    }

    protected void registerEvent(E event) {
        this.events.add(event);
    }

    protected void resetEventsState() {
        events.clear();
    }

    protected List<E> events() {
        return events;
    }
}
