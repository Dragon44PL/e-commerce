package users.user.events;

import users.user.vo.UserInfo;
import java.time.Instant;
import java.util.UUID;

public record UserInformationChangedEvent(Instant occurredOn, UUID aggregateId, UserInfo userInfo) implements UserEvent {

    public UserInformationChangedEvent(UUID userId, UserInfo userInfo) {
        this(Instant.now(), userId, userInfo);
    }
}
