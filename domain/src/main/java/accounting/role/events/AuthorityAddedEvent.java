package accounting.role.events;

import accounting.role.vo.AuthorityId;
import java.util.UUID;

public class AuthorityAddedEvent extends RoleEvent{

    private final AuthorityId authorityId;

    public AuthorityAddedEvent(UUID roleId, AuthorityId authorityId) {
        super(roleId);
        this.authorityId = authorityId;
    }

    public AuthorityId getAuthority() {
        return authorityId;
    }
}
