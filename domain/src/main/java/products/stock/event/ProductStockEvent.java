package products.stock.event;

import domain.events.DomainEvent;
import java.util.UUID;

public interface ProductStockEvent extends DomainEvent<UUID> { }
