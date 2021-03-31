package users.events.user;

import users.snapshot.UserSnapshot;

public class RoleRemovedEvent extends UserEvent {

    public RoleRemovedEvent(UserSnapshot user) {
        super(user);
    }
}
