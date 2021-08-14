package orders.order.event;

import orders.order.vo.OrderStatus;
import orders.order.vo.OrderedProduct;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record OrderCreatedEvent(Instant occurredOn, UUID aggregateId, Set<OrderedProduct> products, OrderStatus orderStatus) implements OrderEvent {

    public OrderCreatedEvent(UUID orderId, Set<OrderedProduct> products, OrderStatus orderStatus) {
        this(Instant.now(), orderId, products, orderStatus);
    }
}
