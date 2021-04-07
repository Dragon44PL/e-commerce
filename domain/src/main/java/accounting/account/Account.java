package accounting.account;

import accounting.account.events.*;
import accounting.account.vo.AccountSnapshot;
import accounting.account.vo.RoleId;
import accounting.role.vo.RoleSnapshot;
import domain.Aggregate;
import accounting.account.exception.PasswordExpiredException;
import accounting.account.vo.Credentials;
import accounting.account.vo.Password;
import domain.AggregateRoot;
import javax.management.relation.Role;
import java.util.*;

class Account extends AggregateRoot<UUID, AccountSnapshot, AccountEvent> {

    private final UUID id;
    private Credentials credentials;
    private String mail;
    private final Set<RoleId> roles;

    static Account create(UUID id, Credentials credentials, String mail, Set<RoleId> roles) {
        final Account account = new Account(id, credentials, mail, roles, new ArrayList<>());
        account.registerEvent(new AccountCreatedEvent(id, credentials, mail, roles));
        return account;
    }

    static Account restore(AccountSnapshot accountSnapshot) {
        return new Account(accountSnapshot.id(), accountSnapshot.credentials(), accountSnapshot.mail(), accountSnapshot.roles(), new ArrayList<>());
    }

    private Account(UUID id, Credentials credentials, String mail, Set<RoleId> roles, List<AccountEvent> accountEvents) {
        super(accountEvents);
        this.id = id;
        this.credentials = credentials;
        this.mail = mail;
        this.roles = new HashSet<>(roles);
    }

    boolean hasRole(RoleId role) {
        return  roles.stream().anyMatch(current -> current.id().compareTo(role.id()) == 0);
    }

    void addRole(RoleId role) {
        if(!hasRole(role))
            processAddingRole(role);
    }

    void changePassword(Password candidate) throws PasswordExpiredException {

        if(isCandidateExpired(candidate)) {
            throw new PasswordExpiredException();
        }

        credentials = credentials.changePassword(candidate);
        final PasswordChangedEvent passwordChangedEvent = new PasswordChangedEvent(id, candidate);
        registerEvent(passwordChangedEvent);
    }

    private boolean isCandidateExpired(Password candidate) {
        final Password current = credentials.getPassword();
        return (candidate.isExpired()) || (current.expireBefore(candidate) && current.isExpired());
    }

    void changeUsername(String username) {
        credentials = credentials.changeUsername(username);
        final UsernameChangedEvent usernameChangedEvent = new UsernameChangedEvent(id, username);
        registerEvent(usernameChangedEvent);
    }

    void processAddingRole(RoleId role) {
        roles.add(role);
        final RoleAddedEvent roleAddedEvent = new RoleAddedEvent(id, role);
        registerEvent(roleAddedEvent);
    }

    void removeRole(RoleId role) {
        if(hasRole(role))
            processRemovingRole(role);
    }

    private void processRemovingRole(RoleId role) {
        roles.removeIf((current) -> current.id().compareTo(role.id()) == 0);
        final RoleRemovedEvent roleRemovedEvent = new RoleRemovedEvent(id, role);
        registerEvent(roleRemovedEvent);
    }

    void changeEmail(String mail) {
        this.mail = mail;
        final MailChangedEvent mailChangedEvent = new MailChangedEvent(id, mail);
        registerEvent(mailChangedEvent);
    }

    @Override
    public AccountSnapshot getSnapshot() {
        final Set<RoleId> currentRoles = new HashSet<>(roles);
        return new AccountSnapshot(id, mail, credentials, currentRoles, new ArrayList<>(events()));
    }
}
