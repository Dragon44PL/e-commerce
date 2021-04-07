package accounting.authority.events;

import domain.events.DomainEvent;
import java.util.UUID;

public interface AuthorityEvent extends DomainEvent<UUID> { }
