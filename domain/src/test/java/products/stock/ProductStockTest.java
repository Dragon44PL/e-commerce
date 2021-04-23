package products.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import products.stock.event.*;
import products.stock.exception.ProductClosedException;
import products.stock.exception.ProductInactiveException;
import products.stock.exception.ProductOutOfStockException;
import products.stock.exception.ProductUnavailableException;
import products.stock.vo.ProductAvailability;
import products.stock.vo.ProductId;
import products.stock.vo.ProductQuantity;
import products.stock.vo.ProductState;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductStockTest {

    private final UUID PRODUCT_STOCK_ID = UUID.randomUUID();

    private final UUID PRODUCT_ID = UUID.randomUUID();
    private final ProductId PRODUCT = new ProductId(PRODUCT_ID);

    private final int QUANTITY = 10;
    private final ProductQuantity PRODUCT_QUANTITY = new ProductQuantity(QUANTITY);

    private final int ZERO = 0;
    private final ProductQuantity ZERO_QUANTITY = new ProductQuantity(ZERO);

    private final int ANOTHER_QUANTITY = 5;
    private final int NEGATIVE_QUANTITY = -5;

    private final ProductState PRODUCT_STATE = ProductState.ACTIVE;

    /**
     *  ProductStock Events
     */

    private final Class<ProductStockCreatedEvent> PRODUCT_STOCK_CREATED_EVENT = ProductStockCreatedEvent.class;
    private final Class<ProductStateChangedEvent> PRODUCT_STATE_CHANGED_EVENT = ProductStateChangedEvent.class;
    private final Class<QuantityChangedEvent> QUANTITY_CHANGED_EVENT = QuantityChangedEvent.class;

    /**
     *  ProductStock Exceptions
     */

    private final Class<ProductUnavailableException> PRODUCT_UNAVAILABLE_EXCEPTION = ProductUnavailableException.class;
    private final Class<ProductClosedException> PRODUCT_CLOSED_EXCEPTION = ProductClosedException.class;
    private final Class<ProductInactiveException> PRODUCT_INACTIVE_EXCEPTION = ProductInactiveException.class;
    private final Class<ProductOutOfStockException> PRODUCT_OUT_OF_STOCK_EXCEPTION = ProductOutOfStockException.class;

    @Test
    @DisplayName("ProductStock Should Create Properly And Generate ProductStockCreatedEvent")
    void productStockShouldCreateProperlyAndGenerateEvent() {
        final ProductStock productStock = ProductStock.create(PRODUCT_STOCK_ID, PRODUCT, PRODUCT_STATE, PRODUCT_QUANTITY);
        Optional<ProductStockEvent> productStockEvent = productStock.findLatestEvent();

        assertTrue(productStockEvent.isPresent());
        assertEquals(PRODUCT_STOCK_CREATED_EVENT, productStockEvent.get().getClass());
    }

    @Test
    @DisplayName("ProductStock Should Restore Properly And Not Generate Event")
    void productStockShouldRestoreProperlyAndNotGenerateEvent() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, PRODUCT_STATE, PRODUCT_QUANTITY);
        final Optional<ProductStockEvent> productStockEvent = productStock.findLatestEvent();

        assertFalse(productStockEvent.isPresent());
    }

    @Test
    @DisplayName("Creating ProductStock with Zero Quantity Should Change Availability to OutOfStock")
    void creatingProductStockWithZeroQuantityShouldChangeAvailability() {
        final ProductStock productStock = ProductStock.create(PRODUCT_STOCK_ID, PRODUCT, PRODUCT_STATE, ZERO_QUANTITY);
        final Optional<ProductStockEvent> productStockEvent = productStock.findLatestEvent();

        final ProductStockCreatedEvent productStockCreatedEvent = (ProductStockCreatedEvent) productStockEvent.get();
        assertEquals(ProductAvailability.OUT_OF_STOCK, productStockCreatedEvent.productAvailability());
    }

    @Test
    @DisplayName("Creating ProductStock with Positive Quantity Should Change Availability to Available")
    void creatingProductStockWithpositiveQuantityShouldChangeAvailability() {
        final ProductStock productStock = ProductStock.create(PRODUCT_STOCK_ID, PRODUCT, PRODUCT_STATE, PRODUCT_QUANTITY);
        final Optional<ProductStockEvent> productStockEvent = productStock.findLatestEvent();

        final ProductStockCreatedEvent productStockCreatedEvent = (ProductStockCreatedEvent) productStockEvent.get();
        assertEquals(ProductAvailability.AVAILABLE, productStockCreatedEvent.productAvailability());
    }

    @Test
    @DisplayName("ProductStock Should Throw ProductClosedException When Increasing")
    void productStockShouldThrowProductClosedExceptionWhenIncreasing() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, ProductState.CLOSED, PRODUCT_QUANTITY);
        assertThrows(PRODUCT_UNAVAILABLE_EXCEPTION, () -> productStock.increaseProductAmount(ANOTHER_QUANTITY));
        assertThrows(PRODUCT_CLOSED_EXCEPTION, () -> productStock.increaseProductAmount(ANOTHER_QUANTITY));
    }

    @Test
    @DisplayName("ProductStock Should Throw ProductInactiveException When Increasing")
    void productStockShouldThrowProductInactiveExceptionWhenIncreasing() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, ProductState.INACTIVE, PRODUCT_QUANTITY);
        assertThrows(PRODUCT_UNAVAILABLE_EXCEPTION, () -> productStock.increaseProductAmount(ANOTHER_QUANTITY));
        assertThrows(PRODUCT_INACTIVE_EXCEPTION, () -> productStock.increaseProductAmount(ANOTHER_QUANTITY));
    }

    @Test
    @DisplayName("ProductStock Should Increase Quantity When Available, Generate QuantityChangeEvent And Not Changing Availability")
    void productStockShouldIncreaseQuantityAndGenerateEvent() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, PRODUCT_STATE, PRODUCT_QUANTITY);
        productStock.increaseProductAmount(ANOTHER_QUANTITY);

        final Optional<ProductStockEvent> productStockEvent = productStock.findLatestEvent();
        assertTrue(productStockEvent.isPresent());
        assertEquals(QUANTITY_CHANGED_EVENT, productStockEvent.get().getClass());

        final QuantityChangedEvent quantityChangedEvent = (QuantityChangedEvent) productStockEvent.get();
        assertEquals(PRODUCT_STOCK_ID, quantityChangedEvent.aggregateId());
        assertEquals(ANOTHER_QUANTITY + QUANTITY, quantityChangedEvent.productQuantity().quantity());
    }

    @Test
    @DisplayName("ProductStock Should Increase Quantity When Out Of Stock, Generate QuantityChange and ProductAvailabilityChanged Events And Change Availability")
    void productStockShouldIncreaseQuantityAndGenerateEventsWhenOutOfStock() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, PRODUCT_STATE, ZERO_QUANTITY);
        productStock.increaseProductAmount(ANOTHER_QUANTITY);

        final List<ProductStockEvent> productStockEvents = productStock.events();
        assertEquals(2, productStockEvents.size());

        final QuantityChangedEvent quantityChangedEvent = (QuantityChangedEvent) productStockEvents.get(0);
        assertNotNull(quantityChangedEvent);
        assertEquals(PRODUCT_STOCK_ID, quantityChangedEvent.aggregateId());
        assertEquals(ZERO_QUANTITY.quantity() + ANOTHER_QUANTITY, quantityChangedEvent.productQuantity().quantity());

        final ProductAvailabilityChangedEvent availabilityEvent = (ProductAvailabilityChangedEvent) productStockEvents.get(1);
        assertNotNull(availabilityEvent);
        assertEquals(PRODUCT_STOCK_ID, availabilityEvent.aggregateId());
        assertEquals(ProductAvailability.AVAILABLE, availabilityEvent.productAvailability());
    }

    @Test
    @DisplayName("ProductStock Should Decrease Quantity When Increasing With Negative Value")
    void productStockShouldDecreaseQuantityWhenIncreasingWithNegativeValue() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, PRODUCT_STATE, PRODUCT_QUANTITY);
        productStock.increaseProductAmount(NEGATIVE_QUANTITY);

        final Optional<ProductStockEvent> productStockEvent = productStock.findLatestEvent();
        assertTrue(productStockEvent.isPresent());
        assertEquals(QUANTITY_CHANGED_EVENT, productStockEvent.get().getClass());

        final QuantityChangedEvent quantityChangedEvent = (QuantityChangedEvent) productStockEvent.get();
        assertEquals(PRODUCT_STOCK_ID, quantityChangedEvent.aggregateId());
        assertEquals(PRODUCT_QUANTITY.quantity() + NEGATIVE_QUANTITY, quantityChangedEvent.productQuantity().quantity());
    }

    @Test
    @DisplayName("ProductStock Should Throw ProductOutOfStockException When Increasing With Negative Value And Quantity Is Negative")
    void productStockShouldThrowExceptionWhenProductIsOutOfStockIncreasing() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, PRODUCT_STATE, ZERO_QUANTITY);
        assertThrows(PRODUCT_OUT_OF_STOCK_EXCEPTION, () -> productStock.increaseProductAmount(NEGATIVE_QUANTITY));
    }

    @Test
    @DisplayName("ProductStock Should Throw ProductClosedException When Decreasing")
    void productStockShouldThrowProductClosedExceptionWhenDecreasing() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, ProductState.CLOSED, PRODUCT_QUANTITY);
        assertThrows(PRODUCT_UNAVAILABLE_EXCEPTION, () -> productStock.decreaseProductAmount(ANOTHER_QUANTITY));
        assertThrows(PRODUCT_CLOSED_EXCEPTION, () -> productStock.decreaseProductAmount(ANOTHER_QUANTITY));
    }

    @Test
    @DisplayName("ProductStock Should Throw ProductInactiveException When Decreasing")
    void productStockShouldThrowProductInactiveExceptionWhenDecreasing() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, ProductState.INACTIVE, PRODUCT_QUANTITY);
        assertThrows(PRODUCT_UNAVAILABLE_EXCEPTION, () -> productStock.decreaseProductAmount(ANOTHER_QUANTITY));
        assertThrows(PRODUCT_INACTIVE_EXCEPTION, () -> productStock.decreaseProductAmount(ANOTHER_QUANTITY));
    }

    @Test
    @DisplayName("ProductStock Should Decrease Quantity When Available, Generate QuantityChangeEvent And Not Changing Availability")
    void productStockShouldDecreaseQuantityAndGenerateEvent() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, PRODUCT_STATE, PRODUCT_QUANTITY);
        productStock.decreaseProductAmount(ANOTHER_QUANTITY);

        final Optional<ProductStockEvent> productStockEvent = productStock.findLatestEvent();
        assertTrue(productStockEvent.isPresent());
        assertEquals(QUANTITY_CHANGED_EVENT, productStockEvent.get().getClass());

        final QuantityChangedEvent quantityChangedEvent = (QuantityChangedEvent) productStockEvent.get();
        assertEquals(PRODUCT_STOCK_ID, quantityChangedEvent.aggregateId());
        assertEquals(QUANTITY - ANOTHER_QUANTITY, quantityChangedEvent.productQuantity().quantity());
    }

    @Test
    @DisplayName("ProductStock Should Increase Quantity When Available, Generate QuantityChange and ProductAvailabilityChanged Events And Change Availability")
    void productStockShouldIncreaseQuantityAndGenerateEventsWhenAvailable() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, PRODUCT_STATE, PRODUCT_QUANTITY);
        productStock.decreaseProductAmount(QUANTITY);

        final List<ProductStockEvent> productStockEvents = productStock.events();
        assertEquals(2, productStockEvents.size());

        final QuantityChangedEvent quantityChangedEvent = (QuantityChangedEvent) productStockEvents.get(0);
        assertNotNull(quantityChangedEvent);
        assertEquals(PRODUCT_STOCK_ID, quantityChangedEvent.aggregateId());
        assertEquals(PRODUCT_QUANTITY.quantity() - QUANTITY, quantityChangedEvent.productQuantity().quantity());

        final ProductAvailabilityChangedEvent availabilityEvent = (ProductAvailabilityChangedEvent) productStockEvents.get(1);
        assertNotNull(availabilityEvent);
        assertEquals(PRODUCT_STOCK_ID, availabilityEvent.aggregateId());
        assertEquals(ProductAvailability.OUT_OF_STOCK, availabilityEvent.productAvailability());
    }

    @Test
    @DisplayName("ProductStock Should Increase Quantity When Decreasing With Negative Value")
    void productStockShouldIncreaseQuantityWhenDecreasingWithNegativeValue() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, PRODUCT_STATE, PRODUCT_QUANTITY);
        productStock.decreaseProductAmount(NEGATIVE_QUANTITY);

        final Optional<ProductStockEvent> productStockEvent = productStock.findLatestEvent();
        assertTrue(productStockEvent.isPresent());
        assertEquals(QUANTITY_CHANGED_EVENT, productStockEvent.get().getClass());

        final QuantityChangedEvent quantityChangedEvent = (QuantityChangedEvent) productStockEvent.get();
        assertEquals(PRODUCT_STOCK_ID, quantityChangedEvent.aggregateId());
        assertEquals(PRODUCT_QUANTITY.quantity() - NEGATIVE_QUANTITY, quantityChangedEvent.productQuantity().quantity());
    }

    @Test
    @DisplayName("ProductStock Should Throw ProductOutOfStockException When Decreasing And Quantity Is Negative")
    void productStockShouldThrowErrorWhenProductIsOutOfStockDecreasing() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, PRODUCT_STATE, ZERO_QUANTITY);
        assertThrows(PRODUCT_OUT_OF_STOCK_EXCEPTION, () -> productStock.decreaseProductAmount(QUANTITY));
    }

    @Test
    @DisplayName("ProductStock Should Be Inactive And Generate ProductStateChangedEvent")
    void productStockShouldBeInactiveAndGenerateEvent() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, PRODUCT_STATE, PRODUCT_QUANTITY);
        productStock.deactivateProduct();

        final Optional<ProductStockEvent> productStockEvent = productStock.findLatestEvent();
        assertTrue(productStockEvent.isPresent());
        assertEquals(PRODUCT_STATE_CHANGED_EVENT, productStockEvent.get().getClass());

        final ProductStateChangedEvent productStateChangedEvent = (ProductStateChangedEvent) productStockEvent.get();
        assertEquals(PRODUCT_STOCK_ID, productStateChangedEvent.aggregateId());
        assertEquals(ProductState.INACTIVE, productStateChangedEvent.productState());
    }

    @Test
    @DisplayName("ProductStock Should Be Inactive And Not Generate Event When Already Inactive")
    void productStockShouldBeInactiveWhenAlreadyInactive() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, PRODUCT_STATE, PRODUCT_QUANTITY);

        productStock.deactivateProduct();
        final Optional<ProductStockEvent> firstProductStockEvent = productStock.findLatestEvent();

        productStock.deactivateProduct();
        final Optional<ProductStockEvent> secondProductStockEvent = productStock.findLatestEvent();

        assertTrue(firstProductStockEvent.isPresent());
        assertTrue(secondProductStockEvent.isPresent());
        assertEquals(firstProductStockEvent.get(), secondProductStockEvent.get());
        assertEquals(1, productStock.events().size());
        assertEquals(PRODUCT_STATE_CHANGED_EVENT, firstProductStockEvent.get().getClass());
    }

    @Test
    @DisplayName("ProductStock Should Throw ProductClosedException When Deactivating")
    void productStockShouldThrowExceptionWhenDeactivating() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, PRODUCT_STATE, PRODUCT_QUANTITY);
        productStock.closeProduct();
        assertThrows(PRODUCT_CLOSED_EXCEPTION, productStock::deactivateProduct);
    }

    @Test
    @DisplayName("ProductStock Should Be Active And Generate ProductStateChangedEvent")
    void productStockShouldBeActiveAndGenerateEvent() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, ProductState.INACTIVE, PRODUCT_QUANTITY);
        productStock.activateProduct();

        final Optional<ProductStockEvent> productStockEvent = productStock.findLatestEvent();
        assertTrue(productStockEvent.isPresent());
        assertEquals(PRODUCT_STATE_CHANGED_EVENT, productStockEvent.get().getClass());

        final ProductStateChangedEvent productStateChangedEvent = (ProductStateChangedEvent) productStockEvent.get();
        assertEquals(PRODUCT_STOCK_ID, productStateChangedEvent.aggregateId());
        assertEquals(ProductState.ACTIVE, productStateChangedEvent.productState());
    }

    @Test
    @DisplayName("ProductStock Should Be Active And Not Generate Event When Already Active")
    void productStockShouldBeActiveAndNotGenerateEvent() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, ProductState.INACTIVE, PRODUCT_QUANTITY);

        productStock.activateProduct();
        final Optional<ProductStockEvent> firstProductStockEvent = productStock.findLatestEvent();

        productStock.activateProduct();
        final Optional<ProductStockEvent> secondProductStockEvent = productStock.findLatestEvent();

        assertTrue(firstProductStockEvent.isPresent());
        assertTrue(secondProductStockEvent.isPresent());
        assertEquals(firstProductStockEvent.get(), secondProductStockEvent.get());
        assertEquals(1, productStock.events().size());
        assertEquals(PRODUCT_STATE_CHANGED_EVENT, firstProductStockEvent.get().getClass());
    }

    @Test
    @DisplayName("ProductStock Should Throw ProductClosedException When Activating")
    void productStockShouldThrowExceptionWhenActivating() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, ProductState.INACTIVE, PRODUCT_QUANTITY);
        productStock.closeProduct();
        assertThrows(PRODUCT_CLOSED_EXCEPTION, productStock::activateProduct);
    }

    @Test
    @DisplayName("ProductStock Should Be Closed And Generate ProductStateChangedEvent")
    void productStockShouldBeClosedAndGenerateEvent() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, PRODUCT_STATE, PRODUCT_QUANTITY);
        productStock.closeProduct();

        final Optional<ProductStockEvent> productStockEvent = productStock.findLatestEvent();
        assertTrue(productStockEvent.isPresent());
        assertEquals(PRODUCT_STATE_CHANGED_EVENT, productStockEvent.get().getClass());

        final ProductStateChangedEvent productStateChangedEvent = (ProductStateChangedEvent) productStockEvent.get();
        assertEquals(PRODUCT_STOCK_ID, productStateChangedEvent.aggregateId());
        assertEquals(ProductState.CLOSED, productStateChangedEvent.productState());
    }

    @Test
    @DisplayName("ProductStock Should Be Closed But Not Generate ProductStateChangedEvent When Is Already Closed")
    void productStockShouldBeClosedButNotGenerateEvent() {
        final ProductStock productStock = ProductStock.restore(PRODUCT_STOCK_ID, PRODUCT, PRODUCT_STATE, PRODUCT_QUANTITY);

        productStock.closeProduct();
        final Optional<ProductStockEvent> firstProductStockEvent = productStock.findLatestEvent();

        productStock.closeProduct();
        final Optional<ProductStockEvent> secondProductStockEvent = productStock.findLatestEvent();

        assertTrue(firstProductStockEvent.isPresent());
        assertTrue(secondProductStockEvent.isPresent());
        assertEquals(firstProductStockEvent.get(), secondProductStockEvent.get());
        assertEquals(1, productStock.events().size());
        assertEquals(PRODUCT_STATE_CHANGED_EVENT, firstProductStockEvent.get().getClass());
    }
}
