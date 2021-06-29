package orders.order.event;

import domain.events.DomainEvent;
import java.util.UUID;

public interface OrderEvent extends DomainEvent<UUID> { }
