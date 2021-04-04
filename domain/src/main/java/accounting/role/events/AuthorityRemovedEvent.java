package accounting.role.events;

import accounting.role.RoleSnapshot;

public class AuthorityRemovedEvent extends RoleEvent {

    public AuthorityRemovedEvent(RoleSnapshot role) {
        super(role);
    }
}
