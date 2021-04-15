package products.stock;

import domain.AggregateRoot;
import products.stock.event.ProductStateChangedEvent;
import products.stock.event.ProductStockCreatedEvent;
import products.stock.event.ProductStockEvent;
import products.stock.event.QuantityChangedEvent;
import products.stock.exception.ProductClosedException;
import products.stock.exception.ProductInactiveException;
import products.stock.exception.ProductUnavailableException;
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
    private ProductQuantity productQuantity;

    public static ProductStock create(UUID id, ProductId product, ProductState productState, ProductQuantity productQuantity) {
        final ProductStock productStock = new ProductStock(id, product, productState, productQuantity, new ArrayList<>());
        productStock.registerEvent(new ProductStockCreatedEvent(id, product, productState, productQuantity));
        return productStock;
    }

    public static ProductStock restore(UUID id, ProductId product, ProductState productState, ProductQuantity productQuantity) {
        return new ProductStock(id, product, productState, productQuantity, new ArrayList<>());
    }

    private ProductStock(UUID id, ProductId product, ProductState productState, ProductQuantity productQuantity, List<ProductStockEvent> events) {
        super(events);
        this.id = id;
        this.product = product;
        this.productState = productState;
        this.productQuantity = productQuantity;
    }

    void increaseProductAmount(int amount) throws ProductUnavailableException {
        this.checkProductState();
        this.processAmountIncreasing(amount);
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

    private void processAmountIncreasing(int amount) {
        this.productQuantity = productQuantity.increaseQuantity(amount);
        final QuantityChangedEvent quantityChangedEvent = new QuantityChangedEvent(id, productQuantity);
        this.registerEvent(quantityChangedEvent);
    }

    void decreaseProductAmount(int amount) throws ProductUnavailableException {
        this.checkProductState();
        this.processAmountDecreasing(amount);
        this.processChangingState();
    }

    private void processAmountDecreasing(int amount) {
        this.productQuantity = productQuantity.decreaseQuantity(amount);
        final QuantityChangedEvent quantityChangedEvent = new QuantityChangedEvent(id, productQuantity);
        this.registerEvent(quantityChangedEvent);
    }

    private void processChangingState() {
        this.productState = (productQuantity.isEmpty()) ? processProductOutOfStock() : processProductNotOutOfStock();
    }

    private ProductState processProductOutOfStock() {
        if(!productState.isOutOfStock()) {
            final ProductState nextState = ProductState.OUT_OF_STOCK;
            final ProductStateChangedEvent productStateChangedEvent = new ProductStateChangedEvent(id, nextState);
            this.registerEvent(productStateChangedEvent);
            return nextState;
        }

        return productState;
    }

    private ProductState processProductNotOutOfStock() {
        if(productState.isOutOfStock() || productState.isActive()) {
            final ProductState nextState = ProductState.AVAILABLE;
            final ProductStateChangedEvent productStateChangedEvent = new ProductStateChangedEvent(id, nextState);
            this.registerEvent(productStateChangedEvent);
            return nextState;
        }

        return productState;
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
