package org.ecommerce.shop.products.product;

import org.ecommerce.shop.finance.vo.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ecommerce.shop.products.product.event.*;
import org.ecommerce.shop.products.product.exception.NegativePriceException;
import org.ecommerce.shop.products.product.vo.CategoryId;
import org.ecommerce.shop.products.product.vo.ProductDetails;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private final CategoryId CATEGORY = new CategoryId(UUID.randomUUID());
    private final Money PRICE = new Money(new BigDecimal(100), Money.DEFAULT_CURRENCY);
    private final ProductDetails PRODUCT_DETAILS = new ProductDetails("Name", "Description");
    private final UUID PRODUCT_ID = UUID.randomUUID();

    private final CategoryId ANOTHER_CATEGORY = new CategoryId(UUID.randomUUID());
    private final ProductDetails ANOTHER_PRODUCT_DETAILS = PRODUCT_DETAILS.changeDescription("Another Description").changeName("Another Name");
    private final Money ANOTHER_PRICE = new Money(new BigDecimal(50), Money.DEFAULT_CURRENCY);
    private final Money ZERO_PRICE = Money.ZERO;
    private final Money NEGATIVE_PRICE = new Money(new BigDecimal(-1), Money.DEFAULT_CURRENCY);

    /*
        Product Events
     */

    private final Class<ProductCreatedEvent> PRODUCT_CREATED_EVENT = ProductCreatedEvent.class;
    private final Class<CategoryChangedEvent> CATEGORY_CHANGED_EVENT = CategoryChangedEvent.class;
    private final Class<PriceChangedEvent> PRICE_CHANGED_EVENT = PriceChangedEvent.class;
    private final Class<ProductDetailsUpdatedEvent> PRODUCT_DETAILS_UPDATED_EVENT = ProductDetailsUpdatedEvent.class;

    /*
        Product Exceptions
     */

    private final Class<NegativePriceException> NEGATIVE_PRICE_EXCEPTION = NegativePriceException.class;

    @Test
    @DisplayName("Product Creation Should Generate ProductCreatedEvent properly")
    void productCreationShouldCreateProperly() {
        final Product product = Product.create(PRODUCT_ID, PRODUCT_DETAILS, CATEGORY, PRICE);
        final Optional<ProductEvent> productEvent = product.findLatestEvent();
        assertTrue(productEvent.isPresent());
        assertEquals(PRODUCT_CREATED_EVENT, productEvent.get().getClass());

        final ProductCreatedEvent productCreatedEvent = (ProductCreatedEvent) productEvent.get();
        assertEquals(productCreatedEvent.aggregateId(), PRODUCT_ID);
        assertEquals(productCreatedEvent.productDetails(), PRODUCT_DETAILS);
        assertEquals(productCreatedEvent.category(), CATEGORY);
        assertEquals(productCreatedEvent.price(), PRICE);
    }

    @Test
    @DisplayName("Product Restore Should Not Generate ProductCreatedEvent")
    void productRestoreShouldNotGenerateEvent() {
        final Product product = Product.restore(PRODUCT_ID, PRODUCT_DETAILS, CATEGORY, PRICE);
        final Optional<ProductEvent> productEvent = product.findLatestEvent();
        assertFalse(productEvent.isPresent());
    }

    @Test
    @DisplayName("Changing Product Category Should Generate CategoryChangedEvent")
    void changingProductCategoryShouldChangeCategoryAndGenerateEvent() {
        final Product product = Product.restore(PRODUCT_ID, PRODUCT_DETAILS, CATEGORY, PRICE);
        product.changeCategory(ANOTHER_CATEGORY);

        final Optional<ProductEvent> productEvent = product.findLatestEvent();
        assertTrue(productEvent.isPresent());
        assertEquals(productEvent.get().getClass(), CATEGORY_CHANGED_EVENT);

        final CategoryChangedEvent categoryChangedEvent = (CategoryChangedEvent) productEvent.get();
        assertEquals(categoryChangedEvent.aggregateId(), PRODUCT_ID);
        assertEquals(categoryChangedEvent.category(), ANOTHER_CATEGORY);
    }

    @Test
    @DisplayName("Changing Product Category With Same Id Should Not Generate CategoryChangedEvent")
    void changingProductCategoryWithSameIdShouldNotChangeCategory() {
        final Product product = Product.restore(PRODUCT_ID, PRODUCT_DETAILS, CATEGORY, PRICE);
        product.changeCategory(CATEGORY);

        final Optional<ProductEvent> productEvent = product.findLatestEvent();
        assertFalse(productEvent.isPresent());
    }

    @Test
    @DisplayName("Product's Price Should Be Changed When Equals 50")
    void productShouldChangePriceAndGenerateEvent() {
        final Product product = Product.restore(PRODUCT_ID, PRODUCT_DETAILS, CATEGORY, PRICE);
        product.changePrice(ANOTHER_PRICE);

        final Optional<ProductEvent> productEvent = product.findLatestEvent();
        assertTrue(productEvent.isPresent());
        assertEquals(productEvent.get().getClass(), PRICE_CHANGED_EVENT);

        final PriceChangedEvent priceChangedEvent = (PriceChangedEvent) productEvent.get();
        assertEquals(priceChangedEvent.price(), ANOTHER_PRICE);
        assertEquals(priceChangedEvent.aggregateId(), PRODUCT_ID);
    }

    @Test
    @DisplayName("Product's Price Should Be Changed When Is Zero")
    void productShouldChangePriceWhenIsZeroAndGenerateEvent() {
        final Product product = Product.restore(PRODUCT_ID, PRODUCT_DETAILS, CATEGORY, PRICE);
        product.changePrice(ZERO_PRICE);

        final Optional<ProductEvent> productEvent = product.findLatestEvent();
        assertTrue(productEvent.isPresent());
        assertEquals(productEvent.get().getClass(), PRICE_CHANGED_EVENT);

        final PriceChangedEvent priceChangedEvent = (PriceChangedEvent) productEvent.get();
        assertEquals(priceChangedEvent.price(), ZERO_PRICE);
        assertEquals(priceChangedEvent.aggregateId(), PRODUCT_ID);
    }

    @Test
    @DisplayName("Product's Price Should Not Be Changed When Is Negative And Throws NegativePriceException")
    void productShouldNotChangePriceWhenIsNegative() {
        final Product product = Product.restore(PRODUCT_ID, PRODUCT_DETAILS, CATEGORY, PRICE);
        assertThrows(NEGATIVE_PRICE_EXCEPTION, () -> product.changePrice(NEGATIVE_PRICE));
    }

    @Test
    @DisplayName("Product's Price Should Not Be Changed When Is The Same")
    void productShouldChangePriceWhenTheSame() {
        final Product product = Product.restore(PRODUCT_ID, PRODUCT_DETAILS, CATEGORY, PRICE);
        product.changePrice(PRICE);

        final Optional<ProductEvent> productEvent = product.findLatestEvent();
        assertFalse(productEvent.isPresent());
    }

    @Test
    @DisplayName("ProductDetails Should Be Changed And Generate ProductDetailsUpdatedEvent")
    void productDetailsShouldBeChangedAndGenerateEvent() {
        final Product product = Product.restore(PRODUCT_ID, PRODUCT_DETAILS, CATEGORY, PRICE);
        product.updateProductDetails(ANOTHER_PRODUCT_DETAILS);

        final Optional<ProductEvent> productEvent = product.findLatestEvent();
        assertTrue(productEvent.isPresent());
        assertEquals(productEvent.get().getClass(), PRODUCT_DETAILS_UPDATED_EVENT);

        final ProductDetailsUpdatedEvent productDetailsUpdatedEvent = (ProductDetailsUpdatedEvent) productEvent.get();
        assertEquals(productDetailsUpdatedEvent.productDetails(), ANOTHER_PRODUCT_DETAILS);
        assertEquals(productDetailsUpdatedEvent.aggregateId(), PRODUCT_ID);
    }

    @Test
    @DisplayName("ProductDetails Should Not Be Changed When The Same")
    void productDetailsShouldNotBeChangedWhenTheSame() {
        final Product product = Product.restore(PRODUCT_ID, PRODUCT_DETAILS, CATEGORY, PRICE);
        product.updateProductDetails(PRODUCT_DETAILS);

        final Optional<ProductEvent> productEvent = product.findLatestEvent();
        assertFalse(productEvent.isPresent());
    }
}
