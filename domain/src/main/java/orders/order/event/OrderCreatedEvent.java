package orders.order.event;

import orders.order.vo.OrderDestination;
import orders.order.vo.OrderStatus;
import orders.order.vo.OrderedProduct;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record OrderCreatedEvent(Instant occurredOn, UUID aggregateId, Set<OrderedProduct> products, OrderDestination destination, OrderStatus orderStatus) implements OrderEvent {

    public OrderCreatedEvent(UUID orderId, Set<OrderedProduct> products, OrderDestination destination, OrderStatus orderStatus) {
        this(Instant.now(), orderId, products, destination, orderStatus);
    }
}
