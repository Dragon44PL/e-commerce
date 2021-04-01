package users.snapshots;

import domain.EntitySnapshot;
import java.util.Set;
import java.util.UUID;

public record RoleSnapshot(UUID id, String name, Set<AuthoritySnapshot> authorities) implements EntitySnapshot<UUID> { }
