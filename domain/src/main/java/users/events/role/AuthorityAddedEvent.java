package users.events.role;

import users.snapshot.RoleSnapshot;

public class AuthorityAddedEvent extends RoleEvent{

    public AuthorityAddedEvent(RoleSnapshot role) {
        super(role);
    }
}
