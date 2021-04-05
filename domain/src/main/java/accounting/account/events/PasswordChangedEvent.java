package accounting.account.events;

import accounting.account.vo.Password;
import java.util.UUID;

public class PasswordChangedEvent extends AccountEvent {

    private final Password password;

    public PasswordChangedEvent(UUID accountId, Password password) {
        super(accountId);
        this.password = password;
    }

    public Password getPassword() {
        return password;
    }
}
