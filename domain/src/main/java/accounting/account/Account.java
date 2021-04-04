package accounting.account;

import accounting.account.events.MailChangedEvent;
import accounting.account.events.PasswordChangedEvent;
import accounting.account.events.RoleAddedEvent;
import accounting.account.events.RoleRemovedEvent;
import accounting.account.vo.RoleId;
import domain.Aggregate;
import accounting.account.exception.PasswordExpiredException;
import accounting.account.vo.Credentials;
import accounting.account.vo.Password;
import java.util.*;

class Account implements Aggregate<UUID, AccountSnapshot> {

    private final UUID id;
    private Credentials credentials;
    private String mail;
    private final Set<RoleId> roles;

    Account(UUID id, Credentials credentials, String mail, Set<RoleId> roles) {
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
        final AccountSnapshot accountSnapshot = this.getSnapshot();
        final PasswordChangedEvent passwordChangedEvent = new PasswordChangedEvent(accountSnapshot);
        return Optional.of(passwordChangedEvent);
    }

    private boolean isCandidateExpired(Password candidate) {
        final Password current = credentials.getPassword();
        return (candidate.isExpired()) || (current.expireBefore(candidate) && current.isExpired());
    }

    private Optional<RoleAddedEvent> processAddingRole(RoleId role) {
        roles.add(role);
        final AccountSnapshot accountSnapshot = this.getSnapshot();
        final RoleAddedEvent roleAddedEvent = new RoleAddedEvent(accountSnapshot);
        return Optional.of(roleAddedEvent);
    }

    Optional<RoleRemovedEvent> removeRole(RoleId role) {
        return (hasRole(role)) ? processRemovingRole(role) : Optional.empty();
    }

    private Optional<RoleRemovedEvent> processRemovingRole(RoleId role) {
        roles.removeIf((current) -> current.id().compareTo(role.id()) == 0);
        final AccountSnapshot accountSnapshot = this.getSnapshot();
        final RoleRemovedEvent roleRemovedEvent = new RoleRemovedEvent(accountSnapshot);
        return Optional.of(roleRemovedEvent);
    }

    Optional<MailChangedEvent> changeEmail(String mail) {
        this.mail = mail;
        final AccountSnapshot accountSnapshot = this.getSnapshot();
        return Optional.of(new MailChangedEvent(accountSnapshot));
    }

    @Override
    public AccountSnapshot getSnapshot() {
        final Set<RoleId> currentRoles = new HashSet<>(roles);
        return new AccountSnapshot(id, mail, credentials, currentRoles);
    }
}
