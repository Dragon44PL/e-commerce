package org.ecommerce.shop.users.user;

import org.ecommerce.shop.core.AggregateRoot;
import org.ecommerce.shop.users.user.events.UserInformationChangedEvent;
import org.ecommerce.shop.users.user.events.UserCreatedEvent;
import org.ecommerce.shop.users.user.events.UserEvent;
import org.ecommerce.shop.users.user.vo.AccountId;
import org.ecommerce.shop.users.user.vo.UserInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class User extends AggregateRoot<UUID, UserEvent> {

    private final UUID id;
    private final AccountId account;
    private UserInfo userInfo;

    static User create(UUID id, AccountId account, UserInfo userInfo) {
        final User user = new User(id, account, userInfo, new ArrayList<>());
        user.registerEvent(new UserCreatedEvent(id, account, userInfo));
        return user;
    }

    static User restore(UUID id, AccountId account, UserInfo userInfo) {
        return new User(id, account, userInfo, new ArrayList<>());
    }

    private User(UUID id, AccountId account, UserInfo userInfo, List<UserEvent> events) {
        super(events);
        this.id = id;
        this.account = account;
        this.userInfo = userInfo;
    }

    void changeUserInformation(UserInfo candidate) {
        if(!sameUserInformation(candidate)) {
            this.userInfo = candidate;
            final UserInformationChangedEvent userInformationChangedEvent = new UserInformationChangedEvent(id, userInfo);
            registerEvent(userInformationChangedEvent);
        }
    }

    private boolean sameUserInformation(UserInfo candidate) {
        return userInfo.equals(candidate);
    }

}
