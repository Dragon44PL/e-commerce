package accounting.role;

import accounting.role.events.RoleCreatedEvent;
import accounting.role.events.RoleEvent;
import accounting.role.vo.AuthorityId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import accounting.role.events.AuthorityAddedEvent;
import accounting.role.events.AuthorityRemovedEvent;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    private final UUID AUTHORITY_ID = UUID.randomUUID();
    private final Set<AuthorityId> AUTHORITIES = Set.of(new AuthorityId(AUTHORITY_ID));

    private final UUID ANOTHER_AUTHORITY_ID = UUID.randomUUID();
    private final AuthorityId ANOTHER_AUTHORITY = new AuthorityId(ANOTHER_AUTHORITY_ID);

    private final UUID ROLE_ID = UUID.randomUUID();
    private final String ROLE_NAME = "Role";

    private final Class<RoleCreatedEvent> ROLE_CREATED_EVENT = RoleCreatedEvent.class;
    private final Class<AuthorityAddedEvent> AUTHORITY_ADDED_EVENT = AuthorityAddedEvent.class;
    private final Class<AuthorityRemovedEvent> AUTHORITY_REMOVED_EVENT = AuthorityRemovedEvent.class;

    @Test
    @DisplayName("Creating Role Should Generate RoleCreateEvent")
    void roleShouldGenerateEventWhenCreate() {
        final Role role = Role.create(ROLE_ID, ROLE_NAME, AUTHORITIES);

        final Optional<RoleEvent> roleEvent = role.findLatestEvent();
        assertTrue(roleEvent.isPresent());
        assertEquals(roleEvent.get().getClass(), ROLE_CREATED_EVENT);

        final RoleCreatedEvent roleCreatedEvent = (RoleCreatedEvent) roleEvent.get();
        assertEquals(roleCreatedEvent.aggregateId(), ROLE_ID);
        assertEquals(roleCreatedEvent.name(), ROLE_NAME);
        assertEquals(roleCreatedEvent.authorities(), AUTHORITIES);
    }

    @Test
    @DisplayName("Role Should Add Authority And Generates AuthorityAddedEvent")
    void roleShouldAddAuthorityAddGenerateEvent() {
        final Role role = Role.restore(ROLE_ID, ROLE_NAME, AUTHORITIES);
        role.addAuthority(ANOTHER_AUTHORITY);
        assertTrue(role.hasAuthority(ANOTHER_AUTHORITY));

        final Optional<RoleEvent> roleEvent = role.findLatestEvent();
        assertTrue(roleEvent.isPresent());
        assertEquals(roleEvent.get().getClass(), AUTHORITY_ADDED_EVENT);

        final AuthorityAddedEvent authorityAddedEvent = (AuthorityAddedEvent) roleEvent.get();
        assertEquals(authorityAddedEvent.aggregateId(), ROLE_ID);
        assertEquals(authorityAddedEvent.authority().id(), ANOTHER_AUTHORITY_ID);
    }

    @Test
    @DisplayName("Role Should Not Add Authority And Not Generate AuthorityAddedEvent")
    void roleShouldNotAddAuthorityAddNotGenerateEvent() {
        final Role role = Role.restore(ROLE_ID, ROLE_NAME, AUTHORITIES);

        role.addAuthority(ANOTHER_AUTHORITY);
        final Optional<RoleEvent> firstRoleEvent = role.findLatestEvent();
        assertTrue(firstRoleEvent.isPresent());
        assertEquals(firstRoleEvent.get().getClass(), AUTHORITY_ADDED_EVENT);

        role.addAuthority(ANOTHER_AUTHORITY);
        final Optional<RoleEvent> secondRoleEvent = role.findLatestEvent();
        assertEquals(firstRoleEvent.get().getClass(), AUTHORITY_ADDED_EVENT);

        assertTrue(role.hasAuthority(ANOTHER_AUTHORITY));

        assertEquals(firstRoleEvent, secondRoleEvent);
        assertEquals(role.events().size(), 1);
    }

    @Test
    @DisplayName("Role Should Not Add Authority When Another Has Same Id")
    void roleShouldNotAddAuthorityWhenNotSameId() {
        final Role role = Role.restore(ROLE_ID, ROLE_NAME, AUTHORITIES);
        final AuthorityId another = new AuthorityId(AUTHORITY_ID);
        role.addAuthority(another);

        final Optional<RoleEvent> roleEvent = role.findLatestEvent();
        assertFalse(roleEvent.isPresent());
    }

    @Test
    @DisplayName("Role Should Remove Authority And Generate AuthorityRemovedEvent")
    void roleShouldRemoveAuthorityAddGenerateEvent() {
        final Role role = Role.restore(ROLE_ID, ROLE_NAME, AUTHORITIES);

        role.addAuthority(ANOTHER_AUTHORITY);
        role.removeAuthority(ANOTHER_AUTHORITY);

        assertFalse(role.hasAuthority(ANOTHER_AUTHORITY));

        final Optional<RoleEvent> roleEvent = role.findLatestEvent();
        assertTrue(roleEvent.isPresent());
        assertEquals(roleEvent.get().getClass(), AUTHORITY_REMOVED_EVENT);

        final AuthorityRemovedEvent authorityRemovedEvent = (AuthorityRemovedEvent) roleEvent.get();
        assertEquals(authorityRemovedEvent.aggregateId(), ROLE_ID);
        assertEquals(authorityRemovedEvent.authority().id(), ANOTHER_AUTHORITY_ID);
    }

    @Test
    @DisplayName("Role Should Not Remove Authority And Not Generate AuthorityRemovedEvent")
    void roleShouldNotRemoveAuthorityAddNotGenerateEvent() {
        final Role role = Role.restore(ROLE_ID, ROLE_NAME, AUTHORITIES);
        role.removeAuthority(ANOTHER_AUTHORITY);

        Optional<RoleEvent> roleEvent = role.findLatestEvent();
        assertFalse(roleEvent.isPresent());
    }

    @Test
    @DisplayName("Restoring Role Should Create Role And Not Generate Event")
    void restoringShouldCreateRoleAndNotGenerateEvent() {
        final Role role = Role.restore(ROLE_ID, ROLE_NAME, AUTHORITIES);

        final Optional<RoleEvent> roleEvent = role.findLatestEvent();
        assertFalse(roleEvent.isPresent());
    }
}
