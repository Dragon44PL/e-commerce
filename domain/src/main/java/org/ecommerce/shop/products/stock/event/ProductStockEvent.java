package org.ecommerce.shop.products.stock.event;

import org.ecommerce.shop.core.events.DomainEvent;
import java.util.UUID;

public interface ProductStockEvent extends DomainEvent<UUID> { }
