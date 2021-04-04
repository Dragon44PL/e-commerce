package accounting.users.events;

import accounting.users.UserSnapshot;

public class PasswordChangedEvent extends UserEvent {

    public PasswordChangedEvent(UserSnapshot user) {
        super(user);
    }
}
