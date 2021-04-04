package accounting.users.events;

import accounting.users.UserSnapshot;

public class RoleRemovedEvent extends UserEvent {

    public RoleRemovedEvent(UserSnapshot user) {
        super(user);
    }
}
