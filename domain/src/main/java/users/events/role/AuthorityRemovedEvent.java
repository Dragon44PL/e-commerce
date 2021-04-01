package users.events.role;

import users.snapshots.RoleSnapshot;

public class AuthorityRemovedEvent extends RoleEvent {

    public AuthorityRemovedEvent(RoleSnapshot role) {
        super(role);
    }
}
