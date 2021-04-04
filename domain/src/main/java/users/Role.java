package users;

import domain.Aggregate;
import users.events.role.AuthorityAddedEvent;
import users.events.role.AuthorityRemovedEvent;
import users.snapshots.AuthoritySnapshot;
import users.snapshots.RoleSnapshot;
import java.util.*;
import java.util.stream.Collectors;

class Role implements Aggregate<UUID, RoleSnapshot> {

    private final UUID id;
    private final String name;
    private final Set<Authority> authorities;

    Role(UUID id, String name, Collection<Authority> authorities) {
        this.id = id;
        this.name = name;
        this.authorities = new HashSet<>(authorities);
    }

    boolean hasAuthority(Authority authority) {
        return authorities.contains(authority) || authorities.stream().anyMatch((current) -> current.hasSameId(authority));
    }

    Optional<AuthorityAddedEvent> addAuthority(Authority authority) {
        return (!hasAuthority(authority)) ? processAddingAuthority(authority) : Optional.empty();
    }

    private Optional<AuthorityAddedEvent> processAddingAuthority(Authority authority) {
        this.authorities.add(authority);
        final RoleSnapshot roleSnapshot = this.getSnapshot();
        final AuthorityAddedEvent authorityAddedEvent = new AuthorityAddedEvent(roleSnapshot);
        return Optional.of(authorityAddedEvent);
    }

    Optional<AuthorityRemovedEvent> removeAuthority(Authority authority) {
        return (hasAuthority(authority)) ? processRemovingAuthority(authority) : Optional.empty();
    }

    private Optional<AuthorityRemovedEvent> processRemovingAuthority(Authority authority) {
        this.authorities.removeIf((current) -> current.hasSameId(authority));
        final RoleSnapshot roleSnapshot = this.getSnapshot();
        final AuthorityRemovedEvent authorityRemovedEvent = new AuthorityRemovedEvent(roleSnapshot);
        return Optional.of(authorityRemovedEvent);
    }

    boolean hasSameId(Role role) {
        return id.compareTo(role.id) == 0;
    }

    @Override
    public RoleSnapshot getSnapshot() {
        final Set<AuthoritySnapshot> authoritiesSnapshot = authorities.stream().map(Authority::getSnapshot).collect(Collectors.toUnmodifiableSet());
        return new RoleSnapshot(id, name, authoritiesSnapshot);
    }
}
