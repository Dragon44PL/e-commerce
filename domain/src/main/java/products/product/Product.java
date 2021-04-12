package products.product;

import domain.AggregateRoot;
import finance.vo.Money;
import products.product.event.PriceChangedEvent;
import products.product.event.ProductCreatedEvent;
import products.product.event.ProductDetailsUpdatedEvent;
import products.product.event.ProductEvent;
import products.product.exception.NegativePriceException;
import products.product.vo.ProductDetails;
import products.product.vo.ProductSnapshot;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

class Product extends AggregateRoot<UUID, ProductSnapshot, ProductEvent> {

    private final UUID id;
    private ProductDetails productDetails;
    private Money price;

    static Product create(UUID id, ProductDetails productDetails, Money price) {
        final Product product = new Product(id, productDetails, price, Collections.emptyList());
        product.registerEvent(new ProductCreatedEvent(id, productDetails, price));
        return product;
    }

    static Product restore(ProductSnapshot productSnapshot) {
        return new Product(productSnapshot.id(), productSnapshot.productDetails(), productSnapshot.price(), productSnapshot.events());
    }

    private Product(UUID id, ProductDetails productDetails, Money price, List<ProductEvent> productEvents) {
        super(productEvents);
        this.id = id;
        this.productDetails = productDetails;
        this.price = price;
    }

    void changePrice(Money price) throws NegativePriceException {
        if(price.lesserThan(Money.ZERO)) {
            throw new NegativePriceException();
        }

        this.price = price;
        final PriceChangedEvent priceChangedEvent = new PriceChangedEvent(id, price);
        this.registerEvent(priceChangedEvent);
    }

    void updateProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
        final ProductDetailsUpdatedEvent productDetailsUpdatedEvent = new ProductDetailsUpdatedEvent(id, productDetails);
        this.registerEvent(productDetailsUpdatedEvent);
    }

    @Override
    public ProductSnapshot getSnapshot() {
        return new ProductSnapshot(id, productDetails, price, events());
    }
}
