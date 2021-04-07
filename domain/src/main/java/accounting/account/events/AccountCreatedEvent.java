package accounting.account.events;

import accounting.account.vo.Credentials;
import accounting.account.vo.RoleId;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record AccountCreatedEvent(Instant occurredOn, UUID aggregateId, Credentials credentials, String mail, Set<RoleId> roles) implements AccountEvent {

    public AccountCreatedEvent(UUID aggregateId, Credentials credentials, String mail, Set<RoleId> roles) {
        this(Instant.now(), aggregateId, credentials, mail, roles);
    }

}
