package users.events.role;

import users.snapshot.RoleSnapshot;

public class AuthorityRemovedEvent extends RoleEvent {

    public AuthorityRemovedEvent(RoleSnapshot role) {
        super(role);
    }
}
