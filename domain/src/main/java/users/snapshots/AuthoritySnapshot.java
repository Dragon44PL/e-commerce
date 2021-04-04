package users.snapshots;

import domain.EntitySnapshot;

import java.util.UUID;

public record AuthoritySnapshot(UUID id, String name) implements EntitySnapshot { }
