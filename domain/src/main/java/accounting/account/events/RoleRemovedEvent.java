package accounting.account.events;

import accounting.account.AccountSnapshot;

public class RoleRemovedEvent extends AccountEvent {

    public RoleRemovedEvent(AccountSnapshot user) {
        super(user);
    }
}
