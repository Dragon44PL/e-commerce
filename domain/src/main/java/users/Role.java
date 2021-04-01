package users;

import domain.Aggregate;
import users.events.role.AuthorityAddedEvent;
import users.events.role.AuthorityRemovedEvent;
import users.events.role.RoleEvent;
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
        return authorities.contains(authority);
    }

    List<RoleEvent> addAuthority(Authority authority) {
        return (!hasAuthority(authority)) ? processAddingAuthority(authority) : new ArrayList<>();
    }

    private List<RoleEvent> processAddingAuthority(Authority authority) {
        this.authorities.add(authority);
        return this.<AuthorityAddedEvent>processEventCreation();
    }

    List<RoleEvent> removeAuthority(Authority authority) {
        return (hasAuthority(authority)) ? processRemovingAuthority(authority) : new ArrayList<>();
    }

    private List<RoleEvent> processRemovingAuthority(Authority authority) {
        this.authorities.remove(authority);
        return this.<AuthorityRemovedEvent>processEventCreation();
    }

    private <T extends RoleEvent> List<RoleEvent> processEventCreation() {
        final RoleSnapshot roleSnapshot = this.getSnapshot();
        final RoleEvent roleEvent = T.ofRoleSnapshot(roleSnapshot);
        return List.of(roleEvent);
    }

    @Override
    public RoleSnapshot getSnapshot() {
        final Set<AuthoritySnapshot> authoritiesSnapshot = authorities.stream().map(Authority::getSnapshot).collect(Collectors.toUnmodifiableSet());
        return new RoleSnapshot(id, name, authoritiesSnapshot);
    }
}
