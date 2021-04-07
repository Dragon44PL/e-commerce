package accounting.account.vo;

import accounting.account.events.AccountEvent;
import domain.vo.EventEntitySnapshot;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record AccountSnapshot(UUID id, String mail, Credentials credentials, Set<RoleId> roles, List<AccountEvent> events) implements EventEntitySnapshot<AccountEvent> { }
