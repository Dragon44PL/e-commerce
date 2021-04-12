package products.product.event;

import finance.vo.Money;
import java.time.Instant;
import java.util.UUID;

public record PriceChangedEvent(Instant occurredOn, UUID aggregateId, Money price) implements ProductEvent {

    public PriceChangedEvent(UUID productId, Money price) {
        this(Instant.now(), productId, price);
    }
}
