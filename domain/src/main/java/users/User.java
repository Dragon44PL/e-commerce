package users;

import domain.AggregateRoot;
import users.events.UserInformationChangedEvent;
import users.events.UserCreatedEvent;
import users.events.UserEvent;
import users.vo.AccountId;
import users.vo.UserInfo;
import users.vo.UserSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class User extends AggregateRoot<UUID, UserSnapshot, UserEvent> {

    private final UUID id;
    private final AccountId account;
    private UserInfo userInfo;

    static User create(UUID id, AccountId account, UserInfo userInfo) {
        final User user = new User(id, account, userInfo, new ArrayList<>());
        user.registerEvent(new UserCreatedEvent(id));
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
