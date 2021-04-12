package products.product.event;

import domain.events.DomainEvent;
import java.util.UUID;

public interface ProductEvent extends DomainEvent<UUID> { }
