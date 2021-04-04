package accounting.account;

import accounting.account.vo.RoleId;
import domain.EntitySnapshot;
import accounting.account.vo.Credentials;
import java.util.Set;
import java.util.UUID;

public record AccountSnapshot(UUID id, String mail, Credentials credentials, Set<RoleId> roles) implements EntitySnapshot { }
