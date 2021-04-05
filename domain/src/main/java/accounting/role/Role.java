package accounting.role;

import accounting.role.vo.AuthorityId;
import domain.Aggregate;
import accounting.role.events.AuthorityAddedEvent;
import accounting.role.events.AuthorityRemovedEvent;
import java.util.*;
import java.util.stream.Collectors;

class Role implements Aggregate<UUID, RoleSnapshot> {

    private final UUID id;
    private final String name;
    private final Set<AuthorityId> authorities;

    Role(UUID id, String name, Collection<AuthorityId> authorities) {
        this.id = id;
        this.name = name;
        this.authorities = new HashSet<>(authorities);
    }

    boolean hasAuthority(AuthorityId authority) {
        return authorities.stream().anyMatch((current) -> current.id().compareTo(authority.id()) == 0);
    }

    Optional<AuthorityAddedEvent> addAuthority(AuthorityId authority) {
        return (!hasAuthority(authority)) ? processAddingAuthority(authority) : Optional.empty();
    }

    private Optional<AuthorityAddedEvent> processAddingAuthority(AuthorityId authority) {
        this.authorities.add(authority);
        final AuthorityAddedEvent authorityAddedEvent = new AuthorityAddedEvent(id, authority);
        return Optional.of(authorityAddedEvent);
    }

    Optional<AuthorityRemovedEvent> removeAuthority(AuthorityId authority) {
        return (hasAuthority(authority)) ? processRemovingAuthority(authority) : Optional.empty();
    }

    private Optional<AuthorityRemovedEvent> processRemovingAuthority(AuthorityId authority) {
        this.authorities.removeIf((current) -> current.id().compareTo(authority.id()) == 0);
        final AuthorityRemovedEvent authorityRemovedEvent = new AuthorityRemovedEvent(id, authority);
        return Optional.of(authorityRemovedEvent);
    }

    @Override
    public RoleSnapshot getSnapshot() {
        final Set<AuthorityId> currentAuthorities = authorities.stream().collect(Collectors.toUnmodifiableSet());
        return new RoleSnapshot(id, name, currentAuthorities);
    }
}
