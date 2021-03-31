package users.events.user;

import users.snapshot.UserSnapshot;

public class RoleAddedEvent extends UserEvent {

    public RoleAddedEvent(UserSnapshot user) {
        super(user);
    }
}
