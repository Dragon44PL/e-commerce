package users.events.role;

import users.snapshots.RoleSnapshot;

public class AuthorityAddedEvent extends RoleEvent{

    public AuthorityAddedEvent(RoleSnapshot role) {
        super(role);
    }
}
