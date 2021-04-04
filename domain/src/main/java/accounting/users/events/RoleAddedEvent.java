package accounting.users.events;


import accounting.users.UserSnapshot;

public class RoleAddedEvent extends UserEvent {

    public RoleAddedEvent(UserSnapshot user) {
        super(user);
    }
}
