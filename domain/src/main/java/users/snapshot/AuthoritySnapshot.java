package users.snapshot;

import domain.EntitySnapshot;

import java.util.UUID;

public record AuthoritySnapshot(UUID id, String name) implements EntitySnapshot<UUID> {

    @Override
    public UUID getId() {
        return id;
    }
}
