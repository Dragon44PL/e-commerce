package accounting.role.events;

import accounting.role.vo.AuthorityId;
import java.util.UUID;

public class AuthorityRemovedEvent extends RoleEvent {

    private final AuthorityId authorityId;

    public AuthorityRemovedEvent(UUID roleId, AuthorityId authorityId) {
        super(roleId);
        this.authorityId = authorityId;
    }

    public AuthorityId getAuthority() {
        return authorityId;
    }
}
