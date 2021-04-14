package products.product.event;

import finance.vo.Money;
import products.product.vo.CategoryId;
import products.product.vo.ProductDetails;
import java.time.Instant;
import java.util.UUID;

public record ProductCreatedEvent(Instant occurredOn, UUID aggregateId, ProductDetails productDetails, CategoryId category, Money price) implements ProductEvent {

    public ProductCreatedEvent(UUID productId, ProductDetails productDetails, CategoryId category,  Money price) {
        this(Instant.now(), productId, productDetails, category, price);
    }
}
