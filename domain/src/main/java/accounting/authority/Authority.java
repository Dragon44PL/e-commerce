package accounting.authority;

import domain.Aggregate;
import java.util.UUID;

class Authority implements Aggregate<UUID, AuthoritySnapshot> {

    private final UUID id;
    private final String name;

    Authority(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public AuthoritySnapshot getSnapshot() {
        return new AuthoritySnapshot(id, name);
    }
}