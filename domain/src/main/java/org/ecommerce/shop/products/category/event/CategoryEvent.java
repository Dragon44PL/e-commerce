package org.ecommerce.shop.products.category.event;

import org.ecommerce.shop.core.events.DomainEvent;
import java.util.UUID;

public interface CategoryEvent extends DomainEvent<UUID> { }
