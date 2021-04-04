package accounting.role.events;

import accounting.role.RoleSnapshot;

public class AuthorityAddedEvent extends RoleEvent{

    public AuthorityAddedEvent(RoleSnapshot role) {
        super(role);
    }
}
