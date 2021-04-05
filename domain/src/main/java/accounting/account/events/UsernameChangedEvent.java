package accounting.account.events;

import java.util.UUID;

public class UsernameChangedEvent extends AccountEvent {

    private final String username;

    public UsernameChangedEvent(UUID accountId, String username) {
        super(accountId);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
