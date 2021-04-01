package users;

import domain.Aggregate;
import users.events.user.*;
import users.exception.CredentialsExpiredException;
import users.snapshots.RoleSnapshot;
import users.snapshots.UserSnapshot;
import users.vo.Credentials;
import users.vo.Password;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

class User implements Aggregate<UUID, UserSnapshot> {

    private UUID id;
    private Credentials credentials;
    private String mail;
    private final Set<Role> roles;

    User(UUID id, Credentials credentials, String mail, Set<Role> roles) {
        this.id = id;
        this.credentials = credentials;
        this.mail = mail;
        this.roles = roles;
    }

    boolean hasAuthority(Authority authority) {
        return roles.stream().anyMatch(role -> role.hasAuthority(authority));
    }

    boolean hasRole(Role role) {
        return roles.contains(role);
    }

    List<UserEvent> addRole(Role role) {
        return (!hasRole(role)) ? processAddingRole(role) : new ArrayList<>();
    }

    List<UserEvent> changePassword(Password candidate) throws CredentialsExpiredException {

        if(isCandidateExpired(candidate)) {
            throw new CredentialsExpiredException();
        }

        credentials = credentials.changePassword(candidate);
        return this.<PasswordChangedEvent>processEventCreation();
    }

    private boolean isCandidateExpired(Password candidate) {
        final Password current = credentials.getPassword();
        return candidate.isExpired() || current.expireBefore(candidate);
    }

    private List<UserEvent> processAddingRole(Role role) {
        roles.add(role);
        return this.<RoleAddedEvent>processEventCreation();
    }

    List<UserEvent> removeRole(Role role) {
        return (hasRole(role)) ? processRemovingRole(role) : new ArrayList<>();
    }

    private List<UserEvent> processRemovingRole(Role role) {
        roles.remove(role);
        return this.<RoleRemovedEvent>processEventCreation();
    }

    List<UserEvent> changeEmail(String mail) {
        this.mail = mail;
        return this.<MailChangedEvent>processEventCreation();
    }

    private <T extends UserEvent> List<UserEvent> processEventCreation() {
        final UserSnapshot userSnapshot = this.getSnapshot();
        final UserEvent userEvent = T.ofUserSnapshot(userSnapshot);
        return List.of(userEvent);
    }

    @Override
    public UserSnapshot getSnapshot() {
        final Set<RoleSnapshot> rolesSnapshot = roles.stream().map(Role::getSnapshot).collect(Collectors.toUnmodifiableSet());
        return new UserSnapshot(id, mail, Credentials.ofCredentials(credentials), rolesSnapshot);
    }
}
