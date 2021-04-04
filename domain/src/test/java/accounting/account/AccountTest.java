package accounting.account;

import accounting.account.vo.RoleId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import accounting.account.events.MailChangedEvent;
import accounting.account.events.PasswordChangedEvent;
import accounting.account.events.RoleAddedEvent;
import accounting.account.events.RoleRemovedEvent;
import accounting.account.exception.PasswordExpiredException;
import accounting.account.vo.Credentials;
import accounting.account.vo.Password;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private final UUID ID = UUID.randomUUID();
    private final UUID ANOTHER_ID = UUID.randomUUID();

    private final RoleId ROLE = new RoleId(ID);
    private final Set<RoleId> ROLES = Set.of(ROLE);

    private final UUID ACCOUNT_ID = UUID.randomUUID();
    private final String ACCOUNT_MAIL = "test@test.pl";
    private final String USERNAME = "USERNAME";
    private final Password PASSWORD = new Password("PASSWORD", Instant.now());
    private final Credentials CREDENTIALS = new Credentials(USERNAME, PASSWORD);

    private final Class<RoleAddedEvent> ROLE_ADDED_EVENT_CLAZZ = RoleAddedEvent.class;
    private final Class<RoleRemovedEvent> ROLE_REMOVED_EVENT_CLAZZ = RoleRemovedEvent.class;
    private final Class<PasswordChangedEvent> PASSWORD_CHANGED_EVENT_CLAZZ = PasswordChangedEvent.class;
    private final Class<MailChangedEvent> MAIL_CHANGED_EVENT_CLAZZ = MailChangedEvent.class;

    @Test
    @DisplayName("Account Should Create Snapshot With Init Values")
    void accountShouldCreateSnapshotWithInitValues() {
        final Account account = new Account(ACCOUNT_ID, CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final AccountSnapshot accountSnapshot = account.getSnapshot();

        assertEquals(accountSnapshot.id(), ACCOUNT_ID);
        assertEquals(accountSnapshot.mail(), ACCOUNT_MAIL);
        assertEquals(accountSnapshot.credentials(), CREDENTIALS);
        assertEquals(accountSnapshot.roles().size(),1);
    }

    @Test
    @DisplayName("Account Should Have Added Role")
    void accountShouldHaveAddedRole() {
        final Account account = new Account(UUID.randomUUID(), CREDENTIALS, ACCOUNT_MAIL, ROLES);
        assertTrue(account.hasRole(ROLE));
    }

    @Test
    @DisplayName("Account Should Not Have Added Role")
    void accountShouldNotHaveAddedRole() {
        final Account account = new Account(UUID.randomUUID(), CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final RoleId anotherRole = new RoleId(ANOTHER_ID);
        assertFalse(account.hasRole(anotherRole));
    }

    @Test
    @DisplayName("Account Should Add Role And Generate Event")
    void accountShouldAddRoleAndGenerateEvent() {
        final Account account = new Account(UUID.randomUUID(), CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final RoleId anotherRole = new RoleId(ANOTHER_ID);
        final Optional<RoleAddedEvent> roleAddedEvent = account.addRole(anotherRole);
        final AccountSnapshot accountSnapshot = account.getSnapshot();

        assertTrue(roleAddedEvent.isPresent());
        assertEquals(roleAddedEvent.get().getClass(), ROLE_ADDED_EVENT_CLAZZ);
        assertEquals(accountSnapshot.roles().size(), 2);
    }

    @Test
    @DisplayName("Account Should Not Add Role And Not Generate Event")
    void accountShouldNotAddRoleAndGenerateEvent() {
        final Account account = new Account(UUID.randomUUID(), CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final RoleId anotherRole = new RoleId(ANOTHER_ID);
        account.addRole(anotherRole);
        final Optional<RoleAddedEvent> roleAddedEvent = account.addRole(anotherRole);
        final AccountSnapshot accountSnapshot = account.getSnapshot();

        assertFalse(roleAddedEvent.isPresent());
        assertEquals(accountSnapshot.roles().size(), 2);
    }

    @Test
    @DisplayName("Account Should Not Add Role When Another Id Is The Same")
    void accountShouldNotAddRoleWhenSameId() {
        final Account account = new Account(UUID.randomUUID(), CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final RoleId anotherRole = new RoleId(ID);
        final Optional<RoleAddedEvent> roleAddedEvent = account.addRole(anotherRole);
        final AccountSnapshot accountSnapshot = account.getSnapshot();

        assertFalse(roleAddedEvent.isPresent());
        assertEquals(accountSnapshot.roles().size(), 1);
    }

    @Test
    @DisplayName("Account Should Remove Role And Generate Event")
    void accountShouldRemoveRoleAndGenerateEvent() {
        final Account account = new Account(UUID.randomUUID(), CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final RoleId anotherRole = new RoleId(ANOTHER_ID);
        account.addRole(anotherRole);
        final Optional<RoleRemovedEvent> roleRemovedEvent = account.removeRole(anotherRole);
        final AccountSnapshot accountSnapshot = account.getSnapshot();

        assertTrue(roleRemovedEvent.isPresent());
        assertEquals(roleRemovedEvent.get().getClass(), ROLE_REMOVED_EVENT_CLAZZ);
        assertEquals(accountSnapshot.roles().size(), 1);
    }

    @Test
    @DisplayName("Account Should Not Remove Role And Not Generate Event")
    void accountShouldNotRemoveRoleAndGenerateEvent() {
        final Account account = new Account(UUID.randomUUID(), CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final RoleId anotherRole = new RoleId(ANOTHER_ID);
        final Optional<RoleRemovedEvent> roleRemovedEvent = account.removeRole(anotherRole);
        final AccountSnapshot accountSnapshot = account.getSnapshot();

        assertFalse(roleRemovedEvent.isPresent());
        assertEquals(accountSnapshot.roles().size(), 1);
    }

    @Test
    @DisplayName("Account's Password Should Be Changed And Generate Event")
    void accountPasswordShouldChangeAndGenerateEvent() {
        final Credentials credentials = new Credentials(USERNAME, new Password("PASSWORD", Instant.now().plus(30, ChronoUnit.SECONDS)));
        final Account account = new Account(UUID.randomUUID(), credentials, ACCOUNT_MAIL, ROLES);
        final Password password = new Password("PASSWORD", Instant.now().plus(60, ChronoUnit.SECONDS));
        final Optional<PasswordChangedEvent> passwordChangedEvent = account.changePassword(password);

        assertTrue(passwordChangedEvent.isPresent());
        assertEquals(passwordChangedEvent.get().getClass(), PASSWORD_CHANGED_EVENT_CLAZZ);
    }

    @Test
    @DisplayName("Account's Password Should Be Changed When New Password Has Lower Expiration Date And Generate Event")
    void accountPasswordShouldChangeWhenLowerAndGenerateEvent() {
        final Credentials credentials = new Credentials(USERNAME, new Password("PASSWORD", Instant.now().plus(60, ChronoUnit.SECONDS)));
        final Account account = new Account(UUID.randomUUID(), credentials, ACCOUNT_MAIL, ROLES);
        final Password password = new Password("PASSWORD", Instant.now().plus(30, ChronoUnit.SECONDS));
        final Optional<PasswordChangedEvent> passwordChangedEvent = account.changePassword(password);

        assertTrue(passwordChangedEvent.isPresent());
        assertEquals(passwordChangedEvent.get().getClass(), PASSWORD_CHANGED_EVENT_CLAZZ);
    }

    @Test
    @DisplayName("Account's Password Should Not Be Changed And Throws Error")
    void accountPasswordShouldThrowException() {
        final Account account = new Account(UUID.randomUUID(), CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final Password password = new Password("PASSWORD", Instant.now().minus(60, ChronoUnit.SECONDS));

        assertThrows(PasswordExpiredException.class, () -> account.changePassword(password));
    }

    @Test
    @DisplayName("Account's Mail Should Be Changed And Generate Event")
    void accountMailShouldBeChangedAndGenerateEvent() {
        final Account account = new Account(UUID.randomUUID(), CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final Optional<MailChangedEvent> mailChangedEvent = account.changeEmail(ACCOUNT_MAIL);

        assertTrue(mailChangedEvent.isPresent());
        assertEquals(mailChangedEvent.get().getClass(), MAIL_CHANGED_EVENT_CLAZZ);
    }
}
