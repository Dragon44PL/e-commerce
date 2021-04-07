package accounting.role.vo;

import accounting.role.events.RoleEvent;
import domain.vo.EventEntitySnapshot;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record RoleSnapshot(UUID id, String name, Set<AuthorityId> authorities, List<RoleEvent> events) implements EventEntitySnapshot<RoleEvent> { }
