package products.product.vo;

import domain.vo.EventEntitySnapshot;
import finance.vo.Money;
import products.product.event.ProductEvent;
import java.util.List;
import java.util.UUID;

public record ProductSnapshot(UUID id, ProductDetails productDetails, Money price, List<ProductEvent> events) implements EventEntitySnapshot<ProductEvent> { }
