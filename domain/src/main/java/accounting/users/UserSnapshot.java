package accounting.users;

import accounting.users.vo.RoleId;
import domain.EntitySnapshot;
import accounting.users.vo.Credentials;
import java.util.Set;
import java.util.UUID;

public record UserSnapshot(UUID id, String mail, Credentials credentials, Set<RoleId> roles) implements EntitySnapshot { }
