package accounting.account.events;


import accounting.account.AccountSnapshot;

public class RoleAddedEvent extends AccountEvent {

    public RoleAddedEvent(AccountSnapshot user) {
        super(user);
    }
}
