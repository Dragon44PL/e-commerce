package users.events.user;


import users.snapshots.UserSnapshot;

public class RoleAddedEvent extends UserEvent {

    public RoleAddedEvent(UserSnapshot user) {
        super(user);
    }
}
