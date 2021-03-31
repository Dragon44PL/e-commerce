package users.events.user;

import users.snapshot.UserSnapshot;

public class PasswordChangedEvent extends UserEvent {

    public PasswordChangedEvent(UserSnapshot user) {
        super(user);
    }
}
