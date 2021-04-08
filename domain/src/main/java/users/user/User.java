package users.user;

import domain.AggregateRoot;
import users.user.events.UserInformationChangedEvent;
import users.user.events.UserCreatedEvent;
import users.user.events.UserEvent;
import users.user.vo.AccountId;
import users.user.vo.UserInfo;
import users.user.vo.UserSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class User extends AggregateRoot<UUID, UserSnapshot, UserEvent> {

    private final UUID id;
    private final AccountId account;
    private UserInfo userInfo;

    static User create(UUID id, AccountId account, UserInfo userInfo) {
        final User user = new User(id, account, userInfo, new ArrayList<>());
        user.registerEvent(new UserCreatedEvent(id, account, userInfo));
        return user;
    }

    static User restore(UserSnapshot userSnapshot) {
        return new User(userSnapshot.id(), userSnapshot.account(), userSnapshot.userInfo(), new ArrayList<>());
    }

    private User(UUID id, AccountId account, UserInfo userInfo, List<UserEvent> events) {
        super(events);
        this.id = id;
        this.account = account;
        this.userInfo = userInfo;
    }

    void changeUserInformation(UserInfo userInfo) {
        this.userInfo = userInfo;
        final UserInformationChangedEvent userInformationChangedEvent = new UserInformationChangedEvent(id, userInfo);
        registerEvent(userInformationChangedEvent);
    }

    @Override
    public UserSnapshot getSnapshot() {
        return new UserSnapshot(id, account, userInfo, new ArrayList<>(events()));
    }
}
