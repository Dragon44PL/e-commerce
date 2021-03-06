package org.ecommerce.shop.accounting.role;

import org.ecommerce.shop.accounting.role.events.RoleCreatedEvent;
import org.ecommerce.shop.accounting.role.events.RoleEvent;
import org.ecommerce.shop.accounting.role.vo.AuthorityId;
import org.ecommerce.shop.accounting.role.events.AuthorityAddedEvent;
import org.ecommerce.shop.accounting.role.events.AuthorityRemovedEvent;
import org.ecommerce.shop.core.AggregateRoot;
import java.util.*;

class Role extends AggregateRoot<UUID, RoleEvent> {

    private final UUID id;
    private final String name;
    private final Set<AuthorityId> authorities;

    static Role create(UUID id, String name, Set<AuthorityId> authorities) {
        final Role role = new Role(id, name, authorities, new ArrayList<>());
        role.registerEvent(new RoleCreatedEvent(id, name, authorities));
        return role;
    }

    static Role restore(UUID id, String name, Set<AuthorityId> authorities) {
        return new Role(id, name, authorities, new ArrayList<>());
    }

    private Role(UUID id, String name, Set<AuthorityId> authorities, List<RoleEvent> roleEvents) {
        super(roleEvents);
        this.id = id;
        this.name = name;
        this.authorities = new HashSet<>(authorities);
    }

    boolean hasAuthority(AuthorityId authority) {
        return authorities.stream().anyMatch((current) -> current.id().compareTo(authority.id()) == 0);
    }

    void addAuthority(AuthorityId authority) {
        if(!hasAuthority(authority))
            processAddingAuthority(authority);
    }

    private void processAddingAuthority(AuthorityId authority) {
        this.authorities.add(authority);
        final AuthorityAddedEvent authorityAddedEvent = new AuthorityAddedEvent(id, authority);
        registerEvent(authorityAddedEvent);
    }

    void removeAuthority(AuthorityId authority) {
        if(hasAuthority(authority))
            processRemovingAuthority(authority);
    }

    private void processRemovingAuthority(AuthorityId authority) {
        this.authorities.removeIf((current) -> current.id().compareTo(authority.id()) == 0);
        final AuthorityRemovedEvent authorityRemovedEvent = new AuthorityRemovedEvent(id, authority);
        registerEvent(authorityRemovedEvent);
    }

}
