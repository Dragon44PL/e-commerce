package domain.vo;

import domain.events.DomainEvent;
import java.util.List;
import java.util.Optional;

public interface EventEntitySnapshot<T extends DomainEvent<?>> extends EntitySnapshot {

    List<T> events();

    default Optional<T> findLatestEvent() {
        final List<T> events = events();
        final int count = events.size();
        return events.stream().skip(count - 1).findFirst();
    }
}
