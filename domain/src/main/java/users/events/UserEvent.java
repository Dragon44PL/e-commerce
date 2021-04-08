package users.events;

import domain.events.DomainEvent;
import java.util.UUID;

public interface UserEvent extends DomainEvent<UUID> { }
