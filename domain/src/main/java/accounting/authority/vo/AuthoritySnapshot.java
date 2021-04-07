package accounting.authority.vo;

import accounting.authority.events.AuthorityEvent;
import domain.vo.EventEntitySnapshot;
import java.util.List;
import java.util.UUID;

public record AuthoritySnapshot(UUID id, String name, List<AuthorityEvent> events) implements EventEntitySnapshot<AuthorityEvent> { }
