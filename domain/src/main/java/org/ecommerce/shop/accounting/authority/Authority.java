package org.ecommerce.shop.accounting.authority;

import org.ecommerce.shop.accounting.authority.events.AuthorityCreatedEvent;
import org.ecommerce.shop.accounting.authority.events.AuthorityEvent;
import org.ecommerce.shop.core.AggregateRoot;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class Authority extends AggregateRoot<UUID, AuthorityEvent> {

    private final UUID id;
    private final String name;

    static Authority create(UUID id, String name) {
        final Authority authority = new Authority(id, name, new ArrayList<>());
        authority.registerEvent(new AuthorityCreatedEvent(id, name));
        return authority;
    }

    static Authority restore(UUID id, String name) {
        return new Authority(id, name, new ArrayList<>());
    }

    private Authority(UUID id, String name, List<AuthorityEvent> authorityEvents) {
        super(authorityEvents);
        this.id = id;
        this.name = name;
    }
}
