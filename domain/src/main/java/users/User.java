package users;

import users.vo.Credentials;
import java.util.Set;
import java.util.UUID;

class User {

    private UUID uuid;
    private Credentials credentials;
    private String mail;
    private final Set<Role> roles;

    public User(Credentials credentials, String mail, Set<Role> roles) {
        this.credentials = credentials;
        this.mail = mail;
        this.roles = roles;
    }

    void addRole(Role role) {
        if(!roles.contains(role)) {
            roles.add(role);
        }
    }

    void removeRole(Role role) {
        if(roles.contains(role)) {
            roles.remove(role);
        }
    }

}
