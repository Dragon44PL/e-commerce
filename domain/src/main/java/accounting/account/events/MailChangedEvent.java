package accounting.account.events;

import java.util.UUID;

public class MailChangedEvent extends AccountEvent {

    public MailChangedEvent(UUID accountId, String mail) {
        super(accountId);
        this.mail = mail;
    }

    private final String mail;

    public String getMail() {
        return mail;
    }
}
