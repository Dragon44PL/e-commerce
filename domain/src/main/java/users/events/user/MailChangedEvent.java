package users.events.user;

import users.snapshots.UserSnapshot;

public class MailChangedEvent extends UserEvent {

    public MailChangedEvent(UserSnapshot user) {
        super(user);
    }
}
