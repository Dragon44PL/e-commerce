package users.events.user;

import users.snapshots.UserSnapshot;

public class PasswordChangedEvent extends UserEvent {

    public PasswordChangedEvent(UserSnapshot user) {
        super(user);
    }
}
