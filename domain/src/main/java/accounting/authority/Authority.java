package accounting.authority;

import accounting.authority.events.AuthorityCreatedEvent;
import accounting.authority.events.AuthorityEvent;
import accounting.authority.vo.AuthoritySnapshot;
import domain.AggregateRoot;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class Authority extends AggregateRoot<UUID, AuthoritySnapshot, AuthorityEvent> {

    private final UUID id;
    private final String name;

    static Authority create(UUID id, String name) {
        final Authority authority = new Authority(id, name, new ArrayList<>());
        authority.registerEvent(new AuthorityCreatedEvent(id, name));
        return authority;
    }

    private Authority(UUID id, String name, List<AuthorityEvent> authorityEvents) {
        super(authorityEvents);
        this.id = id;
        this.name = name;
    }

    @Override
    public AuthoritySnapshot getSnapshot() {
        return new AuthoritySnapshot(id, name, new ArrayList<>(events()));
    }
}
