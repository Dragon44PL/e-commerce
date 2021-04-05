package accounting.account.events;

import domain.events.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public class AccountEvent implements DomainEvent<UUID> {
    private final Instant instant;
    private final UUID accountId;

    public AccountEvent(UUID accountId) {
        this.instant = Instant.now();
        this.accountId = accountId;
    }

    @Override
    public UUID aggregateId() {
        return accountId;
    }

    @Override
    public Instant onInstant() {
        return instant;
    }
}
