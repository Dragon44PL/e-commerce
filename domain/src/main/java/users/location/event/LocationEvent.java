package users.location.event;

import domain.events.DomainEvent;
import java.util.UUID;

public interface LocationEvent extends DomainEvent<UUID> { }
