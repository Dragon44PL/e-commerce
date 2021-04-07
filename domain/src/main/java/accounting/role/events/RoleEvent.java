package accounting.role.events;

import domain.events.DomainEvent;
import java.util.UUID;

public interface RoleEvent extends DomainEvent<UUID> { }
