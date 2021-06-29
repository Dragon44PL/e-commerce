package orders.order;

import domain.AggregateRoot;
import orders.order.event.OrderEvent;
import java.util.List;
import java.util.UUID;

class Order extends AggregateRoot<UUID, OrderEvent> {

    private final UUID id;

    Order(UUID id, List<OrderEvent> events) {
        super(events);
        this.id = id;
    }
}
