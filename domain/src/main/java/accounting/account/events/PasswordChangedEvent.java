package accounting.account.events;

import accounting.account.AccountSnapshot;

public class PasswordChangedEvent extends AccountEvent {

    public PasswordChangedEvent(AccountSnapshot user) {
        super(user);
    }
}
