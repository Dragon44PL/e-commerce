package users.user.events;

import users.user.vo.AccountId;
import users.user.vo.UserInfo;

import java.time.Instant;
import java.util.UUID;

public record UserCreatedEvent(Instant occurredOn, UUID aggregateId, AccountId account, UserInfo userInfo) implements UserEvent {
    public UserCreatedEvent(UUID userId, AccountId accountId, UserInfo userInfo) {
        this(Instant.now(), userId, accountId, userInfo);
    }
}
