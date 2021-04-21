package products.stock;

import domain.AggregateRoot;
import products.stock.event.*;
import products.stock.exception.ProductClosedException;
import products.stock.exception.ProductInactiveException;
import products.stock.exception.ProductUnavailableException;
import products.stock.vo.ProductAvailability;
import products.stock.vo.ProductId;
import products.stock.vo.ProductQuantity;
import products.stock.vo.ProductState;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class ProductStock extends AggregateRoot<UUID, ProductStockEvent> {

    private final UUID id;
    private final ProductId product;
    private ProductState productState;
    private ProductAvailability productAvailability;
    private ProductQuantity productQuantity;

    public static ProductStock create(UUID id, ProductId product, ProductState productState, ProductAvailability productAvailability, ProductQuantity productQuantity) {
        final ProductStock productStock = new ProductStock(id, product, productState, productAvailability, productQuantity, new ArrayList<>());
        productStock.registerEvent(new ProductStockCreatedEvent(id, product, productState, productQuantity));
        return productStock;
    }

    public static ProductStock restore(UUID id, ProductId product, ProductState productState, ProductAvailability productAvailability, ProductQuantity productQuantity) {
        return new ProductStock(id, product, productState, productAvailability, productQuantity, new ArrayList<>());
    }

    private ProductStock(UUID id, ProductId product, ProductState productState, ProductAvailability productAvailability, ProductQuantity productQuantity, List<ProductStockEvent> events) {
        super(events);
        this.id = id;
        this.product = product;
        this.productState = productState;
        this.productAvailability = productAvailability;
        this.productQuantity = productQuantity;
    }

    void increaseProductAmount(int amount) throws ProductUnavailableException {
        this.checkProductState();
        this.processIncreasingWhenPositiveAmount(amount);
        this.processChangingState();
    }

    private void checkProductState() {
        if(productState.isInactive()) {
            throw new ProductInactiveException();
        }

        if(productState.isClosed()) {
            throw new ProductClosedException();
        }
    }

    private void processIncreasingWhenPositiveAmount(int amount) {
        if(ProductQuantity.isQuantityEnough(amount)) {
            this.processAmountIncreasing(amount);
        } else {
            this.processAmountDecreasing(amount);
        }
    }

    private void processAmountIncreasing(int amount) {
        this.productQuantity = productQuantity.increaseQuantity(amount);
        final QuantityChangedEvent quantityChangedEvent = new QuantityChangedEvent(id, productQuantity);
        this.registerEvent(quantityChangedEvent);
    }

    void decreaseProductAmount(int amount) throws ProductUnavailableException {
        this.checkProductState();
        this.processDecreasingWhenPositiveAmount(amount);
        this.processChangingState();
    }

    private void processDecreasingWhenPositiveAmount(int amount) {
        if(ProductQuantity.isQuantityEnough(amount)) {
            this.processAmountDecreasing(amount);
        } else {
            this.processAmountIncreasing(amount);
        }
    }

    private void processAmountDecreasing(int amount) {
        this.productQuantity = productQuantity.decreaseQuantity(amount);
        final QuantityChangedEvent quantityChangedEvent = new QuantityChangedEvent(id, productQuantity);
        this.registerEvent(quantityChangedEvent);
    }

    private void processChangingState() {
        this.productAvailability = (productQuantity.isEmpty()) ? processProductOutOfStock() : processProductNotOutOfStock();
    }

    private ProductAvailability processProductOutOfStock() {
        if(!productAvailability.isOutOfStock()) {
            final ProductAvailability availability = ProductAvailability.OUT_OF_STOCK;
            final ProductAvailabilityChangedEvent productAvailabilityChangedEvent = new ProductAvailabilityChangedEvent(id, availability, productState);
            this.registerEvent(productAvailabilityChangedEvent);
            return availability;
        }

        return productAvailability ;
    }

    private ProductAvailability processProductNotOutOfStock() {
        if(productAvailability.isOutOfStock() || productState.isActive()) {
            final ProductAvailability availability = ProductAvailability.AVAILABLE;
            final ProductAvailabilityChangedEvent productAvailabilityChangedEvent = new ProductAvailabilityChangedEvent(id, availability, productState);
            this.registerEvent(productAvailabilityChangedEvent);
            return availability;
        }

        return productAvailability;
    }

    void inactivate() throws ProductClosedException {
        if(productState.isClosed()) {
            throw new ProductClosedException();
        }

        this.productState = ProductState.INACTIVE;
        final ProductStateChangedEvent productStateChangedEvent = new ProductStateChangedEvent(id, productState);
        this.registerEvent(productStateChangedEvent);
    }

    void activate() throws ProductClosedException {
        if(productState.isClosed()) {
            throw new ProductClosedException();
        }

        this.productState = ProductState.ACTIVE;
        final ProductStateChangedEvent productStateChangedEvent = new ProductStateChangedEvent(id, productState);
        this.registerEvent(productStateChangedEvent);
    }

    void closeProduct() {
        this.productState = ProductState.CLOSED;
        final ProductStateChangedEvent productStateChangedEvent = new ProductStateChangedEvent(id, productState);
        this.registerEvent(productStateChangedEvent);
    }
}
