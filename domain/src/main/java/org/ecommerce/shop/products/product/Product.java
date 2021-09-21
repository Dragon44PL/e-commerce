package org.ecommerce.shop.products.product;

import org.ecommerce.shop.core.AggregateRoot;
import org.ecommerce.shop.finance.vo.Money;
import org.ecommerce.shop.products.product.event.*;
import org.ecommerce.shop.products.product.exception.NegativePriceException;
import org.ecommerce.shop.products.product.vo.CategoryId;
import org.ecommerce.shop.products.product.vo.ProductDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class Product extends AggregateRoot<UUID, ProductEvent> {

    private final UUID id;
    private ProductDetails productDetails;
    private CategoryId category;
    private Money price;

    static Product create(UUID id, ProductDetails productDetails, CategoryId category, Money price) {
        final Product product = new Product(id, productDetails, category, price, new ArrayList<>());
        product.registerEvent(new ProductCreatedEvent(id, productDetails, category, price));
        return product;
    }

    static Product restore(UUID id, ProductDetails productDetails, CategoryId category, Money price) {
        return new Product(id, productDetails, category, price, new ArrayList<>());
    }

    private Product(UUID id, ProductDetails productDetails, CategoryId category, Money price, List<ProductEvent> productEvents) {
        super(productEvents);
        this.id = id;
        this.productDetails = productDetails;
        this.category = category;
        this.price = price;
    }

    void changePrice(Money candidate) throws NegativePriceException {
        if(!samePrice(candidate)) {
            processChangingPrice(candidate);
        }
    }

    private boolean samePrice(Money candidate) {
        return price.equals(candidate);
    }

    private void processChangingPrice(Money price) {
        if(price.lesserThan(Money.ZERO)) {
            throw new NegativePriceException();
        }

        this.price = price;
        final PriceChangedEvent priceChangedEvent = new PriceChangedEvent(id, this.price);
        this.registerEvent(priceChangedEvent);
    }

    void changeCategory(CategoryId candidate) {
        if(!sameCategory(candidate)) {
            this.category = candidate;
            final CategoryChangedEvent categoryChangedEvent = new CategoryChangedEvent(id, category);
            this.registerEvent(categoryChangedEvent);
        }
    }

    private boolean sameCategory(CategoryId candidate) {
        return category.id().compareTo(candidate.id()) == 0;
    }

    void updateProductDetails(ProductDetails candidate) {
        if(!sameProductDetails(candidate)) {
            this.productDetails = candidate;
            final ProductDetailsUpdatedEvent productDetailsUpdatedEvent = new ProductDetailsUpdatedEvent(id, this.productDetails);
            this.registerEvent(productDetailsUpdatedEvent);
        }
    }

    private boolean sameProductDetails(ProductDetails candidate) {
        return productDetails.equals(candidate);
    }
}
