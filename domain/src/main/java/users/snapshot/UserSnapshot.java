package users.snapshot;

import domain.EntitySnapshot;
import users.vo.Credentials;

import java.util.Set;
import java.util.UUID;

public record UserSnapshot(UUID id, String mail, Credentials credentials, Set<RoleSnapshot> roles) implements EntitySnapshot<UUID> {

    @Override
    public UUID getId() {
        return id;
    }
}
