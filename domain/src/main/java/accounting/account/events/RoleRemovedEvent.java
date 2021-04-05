package accounting.account.events;

import accounting.account.vo.RoleId;
import java.util.UUID;

public class RoleRemovedEvent extends AccountEvent {

    private final RoleId roleId;

    public RoleRemovedEvent(UUID accountId, RoleId roleId) {
        super(accountId);
        this.roleId = roleId;
    }

    public RoleId getRole() {
        return roleId;
    }
}
