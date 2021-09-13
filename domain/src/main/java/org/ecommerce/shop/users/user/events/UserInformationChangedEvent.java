package org.ecommerce.shop.users.user.events;

import org.ecommerce.shop.users.user.vo.UserInfo;
import java.time.Instant;
import java.util.UUID;

public record UserInformationChangedEvent(Instant occurredOn, UUID aggregateId, UserInfo userInfo) implements UserEvent {

    public UserInformationChangedEvent(UUID userId, UserInfo userInfo) {
        this(Instant.now(), userId, userInfo);
    }
}
