package org.ecommerce.shop.products.product.event;

import org.ecommerce.shop.core.events.DomainEvent;
import java.util.UUID;

public interface ProductEvent extends DomainEvent<UUID> { }
