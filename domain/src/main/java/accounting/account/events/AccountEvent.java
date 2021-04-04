package accounting.account.events;

import domain.events.DomainEvent;
import accounting.account.AccountSnapshot;

import java.time.Instant;

public class AccountEvent implements DomainEvent {
    private final Instant instant;
    private final AccountSnapshot user;

    public AccountEvent(AccountSnapshot user) {
        this.instant = Instant.now();
        this.user = user;
    }

    public static AccountEvent ofUserSnapshot(AccountSnapshot user) {
        return new AccountEvent(user);
    }

    public Instant onInstant() {
        return instant;
    }

    public AccountSnapshot getUser() {
        return user;
    }
}
