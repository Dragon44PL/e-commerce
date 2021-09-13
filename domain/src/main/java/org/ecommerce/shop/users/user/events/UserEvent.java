package org.ecommerce.shop.users.user.events;

import org.ecommerce.shop.core.events.DomainEvent;
import java.util.UUID;

public interface UserEvent extends DomainEvent<UUID> { }
