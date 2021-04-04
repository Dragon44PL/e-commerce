package accounting.account.events;

import accounting.account.AccountSnapshot;

public class MailChangedEvent extends AccountEvent {

    public MailChangedEvent(AccountSnapshot user) {
        super(user);
    }
}
