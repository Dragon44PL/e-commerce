package users;

import domain.Aggregate;
import users.events.user.*;
import users.exception.PasswordExpiredException;
import users.snapshots.RoleSnapshot;
import users.snapshots.UserSnapshot;
import users.vo.Credentials;
import users.vo.Password;
import java.util.*;
import java.util.stream.Collectors;

class User implements Aggregate<UUID, UserSnapshot> {

    private final UUID id;
    private Credentials credentials;
    private String mail;
    private final Set<Role> roles;

    User(UUID id, Credentials credentials, String mail, Set<Role> roles) {
        this.id = id;
        this.credentials = credentials;
        this.mail = mail;
        this.roles = new HashSet<>(roles);
    }

    boolean hasAuthority(Authority authority) {
        return roles.stream().anyMatch(role -> role.hasAuthority(authority));
    }

    boolean hasRole(Role role) {
        return roles.contains(role) || roles.stream().anyMatch(current -> current.hasSameId(role));
    }

    Optional<RoleAddedEvent> addRole(Role role) {
        return (!hasRole(role)) ? processAddingRole(role) : Optional.empty();
    }

    Optional<PasswordChangedEvent> changePassword(Password candidate) throws PasswordExpiredException {

        if(isCandidateExpired(candidate)) {
            throw new PasswordExpiredException();
        }

        credentials = credentials.changePassword(candidate);
        final UserSnapshot userSnapshot = this.getSnapshot();
        final PasswordChangedEvent passwordChangedEvent = new PasswordChangedEvent(userSnapshot);
        return Optional.of(passwordChangedEvent);
    }

    private boolean isCandidateExpired(Password candidate) {
        final Password current = credentials.getPassword();
        return (candidate.isExpired()) || (current.expireBefore(candidate) && current.isExpired());
    }

    private Optional<RoleAddedEvent> processAddingRole(Role role) {
        roles.add(role);
        final UserSnapshot userSnapshot = this.getSnapshot();
        final RoleAddedEvent roleAddedEvent = new RoleAddedEvent(userSnapshot);
        return Optional.of(roleAddedEvent);
    }

    Optional<RoleRemovedEvent> removeRole(Role role) {
        return (hasRole(role)) ? processRemovingRole(role) : Optional.empty();
    }

    private Optional<RoleRemovedEvent> processRemovingRole(Role role) {
        roles.removeIf((current) -> current.hasSameId(role));
        final UserSnapshot userSnapshot = this.getSnapshot();
        final RoleRemovedEvent roleRemovedEvent = new RoleRemovedEvent(userSnapshot);
        return Optional.of(roleRemovedEvent);
    }

    Optional<MailChangedEvent> changeEmail(String mail) {
        this.mail = mail;
        final UserSnapshot userSnapshot = this.getSnapshot();
        return Optional.of(new MailChangedEvent(userSnapshot));
    }

    @Override
    public UserSnapshot getSnapshot() {
        final Set<RoleSnapshot> rolesSnapshot = roles.stream().map(Role::getSnapshot).collect(Collectors.toUnmodifiableSet());
        return new UserSnapshot(id, mail, credentials, rolesSnapshot);
    }
}
