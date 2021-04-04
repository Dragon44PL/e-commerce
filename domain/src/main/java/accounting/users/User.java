package accounting.users;

import accounting.users.events.MailChangedEvent;
import accounting.users.events.PasswordChangedEvent;
import accounting.users.events.RoleAddedEvent;
import accounting.users.events.RoleRemovedEvent;
import accounting.users.vo.RoleId;
import domain.Aggregate;
import accounting.users.exception.PasswordExpiredException;
import accounting.users.vo.Credentials;
import accounting.users.vo.Password;
import java.util.*;

class User implements Aggregate<UUID, UserSnapshot> {

    private final UUID id;
    private Credentials credentials;
    private String mail;
    private final Set<RoleId> roles;

    User(UUID id, Credentials credentials, String mail, Set<RoleId> roles) {
        this.id = id;
        this.credentials = credentials;
        this.mail = mail;
        this.roles = new HashSet<>(roles);
    }

    boolean hasRole(RoleId role) {
        return  roles.stream().anyMatch(current -> current.id().compareTo(role.id()) == 0);
    }

    Optional<RoleAddedEvent> addRole(RoleId role) {
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

    private Optional<RoleAddedEvent> processAddingRole(RoleId role) {
        roles.add(role);
        final UserSnapshot userSnapshot = this.getSnapshot();
        final RoleAddedEvent roleAddedEvent = new RoleAddedEvent(userSnapshot);
        return Optional.of(roleAddedEvent);
    }

    Optional<RoleRemovedEvent> removeRole(RoleId role) {
        return (hasRole(role)) ? processRemovingRole(role) : Optional.empty();
    }

    private Optional<RoleRemovedEvent> processRemovingRole(RoleId role) {
        roles.removeIf((current) -> current.id().compareTo(role.id()) == 0);
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
        final Set<RoleId> currentRoles = new HashSet<>(roles);
        return new UserSnapshot(id, mail, credentials, currentRoles);
    }
}
