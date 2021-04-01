package users.events.user;

import users.snapshots.UserSnapshot;

public class RoleRemovedEvent extends UserEvent {

    public RoleRemovedEvent(UserSnapshot user) {
        super(user);
    }
}
