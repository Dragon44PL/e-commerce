package accounting.account.events;

import domain.events.DomainEvent;
import java.util.UUID;

public interface AccountEvent extends DomainEvent<UUID> { }
