package org.ecommerce.shop.products.stock;

import org.ecommerce.shop.core.AggregateRoot;
import org.ecommerce.shop.products.stock.event.*;
import org.ecommerce.shop.products.stock.exception.ProductClosedException;
import org.ecommerce.shop.products.stock.exception.ProductInactiveException;
import org.ecommerce.shop.products.stock.exception.ProductUnavailableException;
import org.ecommerce.shop.products.stock.vo.ProductAvailability;
import org.ecommerce.shop.products.stock.vo.ProductId;
import org.ecommerce.shop.products.stock.vo.ProductQuantity;
import org.ecommerce.shop.products.stock.vo.ProductState;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class ProductStock extends AggregateRoot<UUID, ProductStockEvent> {

    private final UUID id;
    private final ProductId product;
    private ProductState productState;
    private ProductAvailability productAvailability;
    private ProductQuantity productQuantity;

    static ProductStock create(UUID id, ProductId product, ProductState productState, ProductQuantity productQuantity) {
        final ProductAvailability productAvailability = predictProductAvailability(productQuantity);
        final ProductStock productStock = new ProductStock(id, product, productState, productAvailability, productQuantity, new ArrayList<>());
        productStock.registerEvent(new ProductStockCreatedEvent(id, product, productState, productAvailability, productQuantity));
        return productStock;
    }

    static ProductStock restore(UUID id, ProductId product, ProductState productState, ProductQuantity productQuantity) {
        final ProductAvailability productAvailability = predictProductAvailability(productQuantity);
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
        this.productAvailability = (productQuantity.isEmpty()) ? processProductOutOfStock() : processProductNotOutOfStock();
    }

    private ProductAvailability processProductOutOfStock() {
        final ProductAvailability availability = ProductAvailability.OUT_OF_STOCK;
        final ProductAvailabilityChangedEvent productAvailabilityChangedEvent = new ProductAvailabilityChangedEvent(id, availability);
        this.registerEvent(productAvailabilityChangedEvent);
        return availability;
    }

    private ProductAvailability processProductNotOutOfStock() {
        if(productAvailability.isOutOfStock()) {
            final ProductAvailability availability = ProductAvailability.AVAILABLE;
            final ProductAvailabilityChangedEvent productAvailabilityChangedEvent = new ProductAvailabilityChangedEvent(id, availability);
            this.registerEvent(productAvailabilityChangedEvent);
            return availability;
        }

        return productAvailability;
    }

    private static ProductAvailability predictProductAvailability(ProductQuantity productQuantity) {
        return productQuantity.isEmpty() ? ProductAvailability.OUT_OF_STOCK : ProductAvailability.AVAILABLE;
    }

    void deactivateProduct() throws ProductClosedException {
        checkProductIsNotClosed();

        if(!productState.isInactive()) {
            this.productState = ProductState.INACTIVE;
            final ProductStateChangedEvent productStateChangedEvent = new ProductStateChangedEvent(id, productState);
            this.registerEvent(productStateChangedEvent);
        }
    }

    void activateProduct() throws ProductClosedException {
        checkProductIsNotClosed();

        if(!productState.isActive()) {
            this.productState = ProductState.ACTIVE;
            final ProductStateChangedEvent productStateChangedEvent = new ProductStateChangedEvent(id, productState);
            this.registerEvent(productStateChangedEvent);
        }
    }

    private void checkProductIsNotClosed() throws ProductClosedException {
        if(productState.isClosed()) {
            throw new ProductClosedException();
        }
    }

    void closeProduct() {
        if(!productState.isClosed()) {
            this.productState = ProductState.CLOSED;
            final ProductStateChangedEvent productStateChangedEvent = new ProductStateChangedEvent(id, productState);
            this.registerEvent(productStateChangedEvent);
        }
    }
}
