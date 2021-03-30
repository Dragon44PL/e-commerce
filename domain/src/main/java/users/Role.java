package users;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class Role {

    private final UUID id;
    private final String name;
    private final Set<Authority> authorities;

    Role(String name, Collection<Authority> authorities) {
        this.name = name;
        this.authorities = new HashSet<>(authorities);
    }

    void addAuthority(Authority authority) {
        if(!authorities.contains(authority)) {
            this.authorities.add(authority);
        }
    }

    void removeAuthority(Authority authority) {
        if(authorities.contains(authority)) {
            this.authorities.remove(authority);
        }
    }

}
