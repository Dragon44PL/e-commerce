package org.ecommerce.shop.order.event;

import org.ecommerce.shop.core.events.DomainEvent;
import java.util.UUID;

public interface OrderEvent extends DomainEvent<UUID> { }
