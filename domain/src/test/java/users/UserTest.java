package users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import users.events.user.MailChangedEvent;
import users.events.user.PasswordChangedEvent;
import users.events.user.RoleAddedEvent;
import users.events.user.RoleRemovedEvent;
import users.exception.PasswordExpiredException;
import users.snapshots.UserSnapshot;
import users.vo.Credentials;
import users.vo.Password;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final UUID ID = UUID.randomUUID();
    private final String NAME = "name";
    private final UUID ANOTHER_ID = UUID.randomUUID();
    private final String ANOTHER_NAME = "Another name";

    private final Authority AUTHORITY = new Authority(ID, NAME);
    private final Set<Authority> AUTHORITIES = Set.of(AUTHORITY);

    private final Role ROLE = new Role(ID, NAME, AUTHORITIES);
    private final Set<Role> ROLES = Set.of(ROLE);

    private final UUID USER_ID = UUID.randomUUID();
    private final String USER_MAIL = "test@test.pl";
    private final String USERNAME = "USERNAME";
    private final Password PASSWORD = new Password("PASSWORD", Instant.now());
    private final Credentials CREDENTIALS = new Credentials(USERNAME, PASSWORD);

    private final Class<RoleAddedEvent> ROLE_ADDED_EVENT_CLAZZ = RoleAddedEvent.class;
    private final Class<RoleRemovedEvent> ROLE_REMOVED_EVENT_CLAZZ = RoleRemovedEvent.class;
    private final Class<PasswordChangedEvent> PASSWORD_CHANGED_EVENT_CLAZZ = PasswordChangedEvent.class;
    private final Class<MailChangedEvent> MAIL_CHANGED_EVENT_CLAZZ = MailChangedEvent.class;

    @Test
    @DisplayName("User Should Create Snapshot With Init Values")
    void userShouldCreateSnapshotWithInitValues() {
        final User user = new User(USER_ID, CREDENTIALS, USER_MAIL, ROLES);
        final UserSnapshot userSnapshot = user.getSnapshot();

        assertEquals(userSnapshot.id(), USER_ID);
        assertEquals(userSnapshot.mail(), USER_MAIL);
        assertEquals(userSnapshot.credentials(), CREDENTIALS);
        assertEquals(userSnapshot.roles().size(),1);
    }

    @Test
    @DisplayName("User Should Have Authority Added Already")
    void userShouldHaveAddedAuthority() {
        final User user = new User(UUID.randomUUID(), CREDENTIALS, USER_MAIL, ROLES);
        assertTrue(user.hasAuthority(AUTHORITY));
    }

    @Test
    @DisplayName("User Should Not Have Authority ")
    void userShouldNotHaveAuthority() {
        final User user = new User(UUID.randomUUID(), CREDENTIALS, USER_MAIL, ROLES);
        final Authority anotherAuthority = new Authority(ANOTHER_ID, ANOTHER_NAME);
        assertFalse(user.hasAuthority(anotherAuthority));
    }

    @Test
    @DisplayName("User Should Have Added Role")
    void userShouldHaveAddedRole() {
        final User user = new User(UUID.randomUUID(), CREDENTIALS, USER_MAIL, ROLES);
        assertTrue(user.hasRole(ROLE));
    }

    @Test
    @DisplayName("User Should Not Have Added Role")
    void userShouldNotHaveAddedRole() {
        final User user = new User(UUID.randomUUID(), CREDENTIALS, USER_MAIL, ROLES);
        final Role anotherRole = new Role(ANOTHER_ID, ANOTHER_NAME, new HashSet<>());
        assertFalse(user.hasRole(anotherRole));
    }

    @Test
    @DisplayName("User Should Add Role And Generate Event")
    void userShouldAddRoleAndGenerateEvent() {
        final User user = new User(UUID.randomUUID(), CREDENTIALS, USER_MAIL, ROLES);
        final Role anotherRole = new Role(ANOTHER_ID, ANOTHER_NAME, new HashSet<>());
        final Optional<RoleAddedEvent> roleAddedEvent = user.addRole(anotherRole);
        final UserSnapshot userSnapshot = user.getSnapshot();

        assertTrue(roleAddedEvent.isPresent());
        assertEquals(roleAddedEvent.get().getClass(), ROLE_ADDED_EVENT_CLAZZ);
        assertEquals(userSnapshot.roles().size(), 2);
    }

    @Test
    @DisplayName("User Should Not Add Role And Not Generate Event")
    void userShouldNotAddRoleAndGenerateEvent() {
        final User user = new User(UUID.randomUUID(), CREDENTIALS, USER_MAIL, ROLES);
        final Role anotherRole = new Role(ANOTHER_ID, ANOTHER_NAME, new HashSet<>());
        user.addRole(anotherRole);
        final Optional<RoleAddedEvent> roleAddedEvent = user.addRole(anotherRole);
        final UserSnapshot userSnapshot = user.getSnapshot();

        assertFalse(roleAddedEvent.isPresent());
        assertEquals(userSnapshot.roles().size(), 2);
    }

    @Test
    @DisplayName("User Should Not Add Role When Another Id Is The Same")
    void userShouldNotAddRoleWhenSameId() {
        final User user = new User(UUID.randomUUID(), CREDENTIALS, USER_MAIL, ROLES);
        final Role anotherRole = new Role(ID, ANOTHER_NAME, new HashSet<>());
        final Optional<RoleAddedEvent> roleAddedEvent = user.addRole(anotherRole);
        final UserSnapshot userSnapshot = user.getSnapshot();

        assertFalse(roleAddedEvent.isPresent());
        assertEquals(userSnapshot.roles().size(), 1);
    }

    @Test
    @DisplayName("User Should Remove Role And Generate Event")
    void userShouldRemoveRoleAndGenerateEvent() {
        final User user = new User(UUID.randomUUID(), CREDENTIALS, USER_MAIL, ROLES);
        final Role anotherRole = new Role(ANOTHER_ID, ANOTHER_NAME, new HashSet<>());
        user.addRole(anotherRole);
        final Optional<RoleRemovedEvent> roleRemovedEvent = user.removeRole(anotherRole);
        final UserSnapshot userSnapshot = user.getSnapshot();

        assertTrue(roleRemovedEvent.isPresent());
        assertEquals(roleRemovedEvent.get().getClass(), ROLE_REMOVED_EVENT_CLAZZ);
        assertEquals(userSnapshot.roles().size(), 1);
    }

    @Test
    @DisplayName("User Should Not Remove Role And Not Generate Event")
    void userShouldNotRemoveRoleAndGenerateEvent() {
        final User user = new User(UUID.randomUUID(), CREDENTIALS, USER_MAIL, ROLES);
        final Role anotherRole = new Role(ANOTHER_ID, ANOTHER_NAME, new HashSet<>());
        final Optional<RoleRemovedEvent> roleRemovedEvent = user.removeRole(anotherRole);
        final UserSnapshot userSnapshot = user.getSnapshot();

        assertFalse(roleRemovedEvent.isPresent());
        assertEquals(userSnapshot.roles().size(), 1);
    }

    @Test
    @DisplayName("User's Password Should Be Changed And Generate Event")
    void userPasswordShouldChangeAndGenerateEvent() {
        final Credentials credentials = new Credentials(USERNAME, new Password("PASSWORD", Instant.now().plus(30, ChronoUnit.SECONDS)));
        final User user = new User(UUID.randomUUID(), credentials, USER_MAIL, ROLES);
        final Password password = new Password("PASSWORD", Instant.now().plus(60, ChronoUnit.SECONDS));
        final Optional<PasswordChangedEvent> passwordChangedEvent = user.changePassword(password);

        assertTrue(passwordChangedEvent.isPresent());
        assertEquals(passwordChangedEvent.get().getClass(), PASSWORD_CHANGED_EVENT_CLAZZ);
    }

    @Test
    @DisplayName("User's Password Should Be Changed When New Password Has Lower Expiration Date And Generate Event")
    void userPasswordShouldChangeWhenLowerAndGenerateEvent() {
        final Credentials credentials = new Credentials(USERNAME, new Password("PASSWORD", Instant.now().plus(60, ChronoUnit.SECONDS)));
        final User user = new User(UUID.randomUUID(), credentials, USER_MAIL, ROLES);
        final Password password = new Password("PASSWORD", Instant.now().plus(30, ChronoUnit.SECONDS));
        final Optional<PasswordChangedEvent> passwordChangedEvent = user.changePassword(password);

        assertTrue(passwordChangedEvent.isPresent());
        assertEquals(passwordChangedEvent.get().getClass(), PASSWORD_CHANGED_EVENT_CLAZZ);
    }

    @Test
    @DisplayName("User's Password Should Not Be Changed And Throws Error")
    void userPasswordShouldThrowException() {
        final User user = new User(UUID.randomUUID(), CREDENTIALS, USER_MAIL, ROLES);
        final Password password = new Password("PASSWORD", Instant.now().minus(60, ChronoUnit.SECONDS));

        assertThrows(PasswordExpiredException.class, () -> user.changePassword(password));
    }

    @Test
    @DisplayName("User's Mail Should Be Changed And Generate Event")
    void userMailShouldBeChangedAndGenerateEvent() {
        final User user = new User(UUID.randomUUID(), CREDENTIALS, USER_MAIL, ROLES);
        final Optional<MailChangedEvent> mailChangedEvent = user.changeEmail(USER_MAIL);

        assertTrue(mailChangedEvent.isPresent());
        assertEquals(mailChangedEvent.get().getClass(), MAIL_CHANGED_EVENT_CLAZZ);
    }
}
