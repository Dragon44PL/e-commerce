package users.events.user;

import users.snapshot.UserSnapshot;

public class MailChangedEvent extends UserEvent {

    public MailChangedEvent(UserSnapshot user) {
        super(user);
    }
}
