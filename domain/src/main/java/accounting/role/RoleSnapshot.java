package accounting.role;

import accounting.role.vo.AuthorityId;
import domain.EntitySnapshot;
import java.util.Set;
import java.util.UUID;

public record RoleSnapshot(UUID id, String name, Set<AuthorityId> authorities) implements EntitySnapshot { }
