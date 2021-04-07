package accounting.account;

import accounting.account.events.*;
import accounting.account.vo.AccountSnapshot;
import accounting.account.vo.RoleId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import accounting.account.exception.PasswordExpiredException;
import accounting.account.vo.Credentials;
import accounting.account.vo.Password;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

    private final String ANOTHER_USERNAME = "ANOTHER USERNAME";
    private final Password ANOTHER_PASSWORD = new Password("PASSWORD", Instant.now().plus(60, ChronoUnit.SECONDS));
    private final String ANOTHER_ACCOUNT_MAIL = "ANOTHER_MAIL";

    private final Class<RoleAddedEvent> ROLE_ADDED_EVENT = RoleAddedEvent.class;
    private final Class<RoleRemovedEvent> ROLE_REMOVED_EVENT = RoleRemovedEvent.class;
    private final Class<PasswordChangedEvent> PASSWORD_CHANGED_EVENT = PasswordChangedEvent.class;
    private final Class<UsernameChangedEvent> USERNAME_CHANGED_EVENT = UsernameChangedEvent.class;
    private final Class<MailChangedEvent> MAIL_CHANGED_EVENT = MailChangedEvent.class;

    @Test
    @DisplayName("Account Should Create Snapshot With Init Values")
    void accountShouldCreateSnapshotWithInitValues() {
        final Account account = Account.create(ACCOUNT_ID, CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final AccountSnapshot accountSnapshot = account.getSnapshot();

        assertEquals(accountSnapshot.id(), ACCOUNT_ID);
        assertEquals(accountSnapshot.mail(), ACCOUNT_MAIL);
        assertEquals(accountSnapshot.credentials(), CREDENTIALS);
        assertEquals(accountSnapshot.roles().size(),1);
    }

    @Test
    @DisplayName("Account Should Have Added Role")
    void accountShouldHaveAddedRole() {
        final Account account = Account.create(ACCOUNT_ID, CREDENTIALS, ACCOUNT_MAIL, ROLES);
        assertTrue(account.hasRole(ROLE));
    }

    @Test
    @DisplayName("Account Should Not Have Added Role")
    void accountShouldNotHaveAddedRole() {
        final Account account = Account.create(ACCOUNT_ID, CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final RoleId anotherRole = new RoleId(ANOTHER_ID);
        assertFalse(account.hasRole(anotherRole));
    }

    @Test
    @DisplayName("Account Should Add Role And Generate Event")
    void accountShouldAddRoleAndGenerateEvent() {
        final Account account = Account.create(ACCOUNT_ID, CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final RoleId anotherRole = new RoleId(ANOTHER_ID);
        account.addRole(anotherRole);
        final AccountSnapshot accountSnapshot = account.getSnapshot();

        final Optional<AccountEvent> accountEvent = accountSnapshot.findLatestEvent();
        assertTrue(accountEvent.isPresent());
        assertEquals(accountEvent.get().getClass(), ROLE_ADDED_EVENT);

        final RoleAddedEvent roleAddedEvent = (RoleAddedEvent) accountEvent.get();
        assertEquals(roleAddedEvent.role(), anotherRole);
        assertEquals(roleAddedEvent.aggregateId(), ACCOUNT_ID);
        assertEquals(accountSnapshot.roles().size(), 2);
    }

    @Test
    @DisplayName("Account Should Not Add Role And Not Generate Event")
    void accountShouldNotAddRoleAndGenerateEvent() {
        final Account account = Account.create(ACCOUNT_ID, CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final RoleId anotherRole = new RoleId(ANOTHER_ID);

        account.addRole(anotherRole);
        final AccountSnapshot firstSnapshot = account.getSnapshot();

        account.addRole(anotherRole);
        final AccountSnapshot secondSnapshot = account.getSnapshot();

        final List<AccountEvent> firstSnapshotEvents = firstSnapshot.events();
        final List<AccountEvent> secondSnapshotEvents = secondSnapshot.events();
        assertEquals(firstSnapshotEvents.size(), secondSnapshotEvents.size());
    }

    @Test
    @DisplayName("Account Should Not Add Role When Another Id Is The Same")
    void accountShouldNotAddRoleWhenSameId() {
        final Account account = Account.create(ACCOUNT_ID, CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final RoleId anotherRole = new RoleId(ID);
        account.addRole(anotherRole);
        final AccountSnapshot accountSnapshot = account.getSnapshot();

        final Optional<AccountEvent> accountEvent = accountSnapshot.findLatestEvent();
        assertTrue(accountEvent.isPresent());
        assertNotEquals(accountEvent.get().getClass(), ROLE_ADDED_EVENT);
        assertEquals(accountSnapshot.roles().size(), 1);
    }

    @Test
    @DisplayName("Account Should Remove Role And Generate Event")
    void accountShouldRemoveRoleAndGenerateEvent() {
        final Account account = Account.create(ACCOUNT_ID, CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final RoleId anotherRole = new RoleId(ANOTHER_ID);
        account.addRole(anotherRole);
        account.removeRole(anotherRole);
        final AccountSnapshot accountSnapshot = account.getSnapshot();

        final Optional<AccountEvent> accountEvent = accountSnapshot.findLatestEvent();
        assertTrue(accountEvent.isPresent());
        assertEquals(accountEvent.get().getClass(), ROLE_REMOVED_EVENT);

        final RoleRemovedEvent roleRemovedEvent = (RoleRemovedEvent) accountEvent.get();
        assertEquals(roleRemovedEvent.role(), anotherRole);
        assertEquals(roleRemovedEvent.aggregateId(), ACCOUNT_ID);
        assertEquals(accountSnapshot.roles().size(), 1);
    }

    @Test
    @DisplayName("Account Should Not Remove Role And Not Generate Event")
    void accountShouldNotRemoveRoleAndGenerateEvent() {
        final Account account = Account.create(ACCOUNT_ID, CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final RoleId anotherRole = new RoleId(ANOTHER_ID);

        account.removeRole(anotherRole);
        final AccountSnapshot accountSnapshot = account.getSnapshot();
        final Optional<AccountEvent> accountEvent = accountSnapshot.findLatestEvent();

        assertTrue(accountEvent.isPresent());
        assertNotEquals(accountEvent.get().getClass(), ROLE_REMOVED_EVENT);
        assertEquals(accountSnapshot.roles().size(), 1);
    }

    @Test
    @DisplayName("Account's Password Should Be Changed And Generate Event")
    void accountPasswordShouldChangeAndGenerateEvent() {
        final Credentials credentials = new Credentials(USERNAME, new Password("PASSWORD", Instant.now().plus(30, ChronoUnit.SECONDS)));
        final Account account = Account.create(ACCOUNT_ID, credentials, ACCOUNT_MAIL, ROLES);
        account.changePassword(ANOTHER_PASSWORD);

        final Optional<AccountEvent> accountEvent = account.getSnapshot().findLatestEvent();
        assertTrue(accountEvent.isPresent());
        assertEquals(accountEvent.get().getClass(), PASSWORD_CHANGED_EVENT);

        final PasswordChangedEvent passwordChangedEvent = (PasswordChangedEvent) accountEvent.get();
        assertEquals(passwordChangedEvent.aggregateId(), ACCOUNT_ID);
        assertEquals(passwordChangedEvent.password(), ANOTHER_PASSWORD);
    }

    @Test
    @DisplayName("Account's Password Should Be Changed When New Password Has Lower Expiration Date And Generate Event")
    void accountPasswordShouldChangeWhenLowerAndGenerateEvent() {
        final Credentials credentials = new Credentials(USERNAME, new Password("PASSWORD", Instant.now().plus(60, ChronoUnit.SECONDS)));
        final Account account = Account.create(ACCOUNT_ID, credentials, ACCOUNT_MAIL, ROLES);
        account.changePassword(ANOTHER_PASSWORD);

        final Optional<AccountEvent> accountEvent = account.getSnapshot().findLatestEvent();
        assertTrue(accountEvent.isPresent());
        assertEquals(accountEvent.get().getClass(), PASSWORD_CHANGED_EVENT);

        final PasswordChangedEvent passwordChangedEvent = (PasswordChangedEvent) accountEvent.get();
        assertEquals(passwordChangedEvent.aggregateId(), ACCOUNT_ID);
        assertEquals(passwordChangedEvent.password(), ANOTHER_PASSWORD);
    }

    @Test
    @DisplayName("Account's Password Should Not Be Changed And Throws Error")
    void accountPasswordShouldThrowException() {
        final Account account = Account.create(ACCOUNT_ID, CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final Password password = new Password("PASSWORD", Instant.now().minus(60, ChronoUnit.SECONDS));

        assertThrows(PasswordExpiredException.class, () -> account.changePassword(password));
    }

    @Test
    @DisplayName("Account's Username Should Be Change And Generate Event")
    void accountUsernameShouldChangeAndGenerateEvent() {
        final Account account = Account.create(ACCOUNT_ID, CREDENTIALS, ACCOUNT_MAIL, ROLES);
        account.changeUsername(ANOTHER_USERNAME);

        final Optional<AccountEvent> accountEvent = account.getSnapshot().findLatestEvent();
        assertTrue(accountEvent.isPresent());
        assertEquals(accountEvent.get().getClass(), USERNAME_CHANGED_EVENT);

        final UsernameChangedEvent usernameChangedEvent = (UsernameChangedEvent) accountEvent.get();
        assertEquals(usernameChangedEvent.aggregateId(), ACCOUNT_ID);
        assertEquals(usernameChangedEvent.username(), ANOTHER_USERNAME);
    }

    @Test
    @DisplayName("Account's Mail Should Be Changed And Generate Event")
    void accountMailShouldBeChangedAndGenerateEvent() {
        final Account account = Account.create(ACCOUNT_ID, CREDENTIALS, ACCOUNT_MAIL, ROLES);
        account.changeEmail(ANOTHER_ACCOUNT_MAIL);

        final Optional<AccountEvent> accountEvent = account.getSnapshot().findLatestEvent();
        assertTrue(accountEvent.isPresent());
        assertEquals(accountEvent.get().getClass(), MAIL_CHANGED_EVENT);

        final MailChangedEvent mailChangedEvent = (MailChangedEvent)  accountEvent.get();
        assertEquals(mailChangedEvent.aggregateId(), ACCOUNT_ID);
        assertEquals(mailChangedEvent.mail(), ANOTHER_ACCOUNT_MAIL);
    }

    @Test
    @DisplayName("Modifying Account Snapshot Events Should Not Touch Account Events")
    void modifyingSnapshotEventsShouldNotTouchAggregateEvents() {
        final Account account = Account.create(ACCOUNT_ID, CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final List<AccountEvent> modifiedAccountEvents = account.getSnapshot().events();
        modifiedAccountEvents.clear();

        final List<AccountEvent> originalAccountEvents = account.getSnapshot().events();
        assertNotEquals(modifiedAccountEvents.size(), originalAccountEvents.size());
    }

    @Test
    @DisplayName("Restoring Account Should Create Account With Values From Account Snapshot")
    void restoringShouldCreateAccountWithValuesFromSnapshot() {
        final Account account = Account.create(ACCOUNT_ID, CREDENTIALS, ACCOUNT_MAIL, ROLES);
        final AccountSnapshot accountSnapshot = account.getSnapshot();

        final Account anotherAccount = Account.restore(accountSnapshot);
        final AccountSnapshot anotherAccountSnapshot = anotherAccount.getSnapshot();

        assertEquals(accountSnapshot.id(), anotherAccountSnapshot.id());
        assertEquals(accountSnapshot.credentials(), anotherAccountSnapshot.credentials());
        assertEquals(accountSnapshot.mail(), anotherAccountSnapshot.mail());
        assertEquals(accountSnapshot.roles(), anotherAccountSnapshot.roles());
        assertNotEquals(accountSnapshot.events().size(), anotherAccountSnapshot.events().size());
    }

}
