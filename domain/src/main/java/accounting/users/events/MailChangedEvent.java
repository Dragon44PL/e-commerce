package accounting.users.events;

import accounting.users.UserSnapshot;

public class MailChangedEvent extends UserEvent {

    public MailChangedEvent(UserSnapshot user) {
        super(user);
    }
}
