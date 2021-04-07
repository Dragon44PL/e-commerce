package accounting.role;

import accounting.role.events.RoleCreatedEvent;
import accounting.role.events.RoleEvent;
import accounting.role.vo.AuthorityId;
import accounting.role.vo.RoleSnapshot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import accounting.role.events.AuthorityAddedEvent;
import accounting.role.events.AuthorityRemovedEvent;
import java.util.List;
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
    @DisplayName("Role Should Create Snapshot With Init Values")
    void roleShouldCreateSnapshotWithInitValues() {
        final Role role = Role.create(ROLE_ID, ROLE_NAME, AUTHORITIES);
        final RoleSnapshot roleSnapshot = role.getSnapshot();

        assertNotNull(roleSnapshot);
        assertEquals(roleSnapshot.id().compareTo(ROLE_ID), 0);
        assertEquals(roleSnapshot.name(), ROLE_NAME);
        assertEquals(roleSnapshot.authorities().size(), AUTHORITIES.size());
    }

    @Test
    @DisplayName("Creating Role Should Generate RoleCreateEvent")
    void roleShouldGenerateEventWhenCreate() {
        final Role role = Role.create(ROLE_ID, ROLE_NAME, AUTHORITIES);
        final RoleSnapshot roleSnapshot = role.getSnapshot();
        final List<RoleEvent> roleEvents =  roleSnapshot.events();
        assertNotNull(roleEvents);
        assertFalse(roleEvents.isEmpty());

        final Optional<RoleEvent> roleEvent = roleSnapshot.findLatestEvent();
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
        final Role role = Role.create(ROLE_ID, ROLE_NAME, AUTHORITIES);
        role.addAuthority(ANOTHER_AUTHORITY);
        final RoleSnapshot roleSnapshot = role.getSnapshot();

        final Optional<RoleEvent> roleEvent = roleSnapshot.findLatestEvent();
        assertTrue(roleEvent.isPresent());
        assertEquals(roleEvent.get().getClass(), AUTHORITY_ADDED_EVENT);

        final AuthorityAddedEvent authorityAddedEvent = (AuthorityAddedEvent) roleEvent.get();
        assertEquals(authorityAddedEvent.aggregateId(), ROLE_ID);
        assertEquals(authorityAddedEvent.authority().id(), ANOTHER_AUTHORITY_ID);
        assertEquals(roleSnapshot.authorities().size(), 2);
    }

    @Test
    @DisplayName("Role Should Not Add Authority And Not Generate AuthorityAddedEvent")
    void roleShouldNotAddAuthorityAddNotGenerateEvent() {
        final Role role = Role.create(ROLE_ID, ROLE_NAME, AUTHORITIES);

        role.addAuthority(ANOTHER_AUTHORITY);
        final RoleSnapshot firstSnapshot = role.getSnapshot();

        role.addAuthority(ANOTHER_AUTHORITY);
        final RoleSnapshot secondSnapshot = role.getSnapshot();

        final List<RoleEvent> firstSnapshotEvents = firstSnapshot.events();
        final List<RoleEvent> secondSnapshotEvents = secondSnapshot.events();
        assertEquals(firstSnapshotEvents.size(), secondSnapshotEvents.size());
    }

    @Test
    @DisplayName("Role Should Not Add Authority When Another Has Same Id")
    void roleShouldNotAddAuthorityWhenNotSameId() {
        final Role role = Role.create(ROLE_ID, ROLE_NAME, AUTHORITIES);
        final AuthorityId another = new AuthorityId(AUTHORITY_ID);
        role.addAuthority(another);
        final RoleSnapshot roleSnapshot = role.getSnapshot();
        final Optional<RoleEvent> roleEvent = roleSnapshot.findLatestEvent();

        assertTrue(roleEvent.isPresent());
        assertEquals(roleEvent.get().getClass(), ROLE_CREATED_EVENT);
        assertEquals(roleSnapshot.authorities().size(), 1);
    }

    @Test
    @DisplayName("Role Should Remove Authority And  Generate AuthorityRemovedEvent")
    void roleShouldRemoveAuthorityAddGenerateEvent() {
        final Role role = Role.create(ROLE_ID, ROLE_NAME, AUTHORITIES);

        role.addAuthority(ANOTHER_AUTHORITY);
        role.removeAuthority(ANOTHER_AUTHORITY);
        final RoleSnapshot roleSnapshot = role.getSnapshot();

        final Optional<RoleEvent> roleEvent = roleSnapshot.findLatestEvent();
        assertTrue(roleEvent.isPresent());
        assertEquals(roleEvent.get().getClass(), AUTHORITY_REMOVED_EVENT);

        final AuthorityRemovedEvent authorityRemovedEvent = (AuthorityRemovedEvent) roleEvent.get();
        assertEquals(authorityRemovedEvent.aggregateId(), ROLE_ID);
        assertEquals(authorityRemovedEvent.authority().id(), ANOTHER_AUTHORITY_ID);
        assertEquals(roleSnapshot.authorities().size(), 1);
    }

    @Test
    @DisplayName("Role Should Not Remove Authority And Not Generate AuthorityRemovedEvent")
    void roleShouldNotRemoveAuthorityAddNotGenerateEvent() {
        final Role role = Role.create(ROLE_ID, ROLE_NAME, AUTHORITIES);

        role.removeAuthority(ANOTHER_AUTHORITY);
        final RoleSnapshot roleSnapshot = role.getSnapshot();

        Optional<RoleEvent> roleEvent = roleSnapshot.findLatestEvent();
        assertTrue(roleEvent.isPresent());
        assertNotEquals(roleEvent.get().getClass(), AUTHORITY_REMOVED_EVENT);
        assertEquals(roleSnapshot.authorities().size(), 1);
    }

    @Test
    @DisplayName("Modifying Role Snapshot Events Should Not Touch Role Events")
    void modifyingSnapshotEventsShouldNotTouchAggregateEvents() {
        final Role role = Role.create(ROLE_ID, ROLE_NAME, AUTHORITIES);
        final List<RoleEvent> modifiedRoleEvents = role.getSnapshot().events();
        modifiedRoleEvents.clear();

        final List<RoleEvent> originalRoleEvents = role.getSnapshot().events();
        assertNotEquals(modifiedRoleEvents.size(), originalRoleEvents.size());
    }

    @Test
    @DisplayName("Restoring Role Should Create Account With Values From Role Snapshot")
    void restoringShouldCreateRoleWithValuesFromSnapshot() {
        final Role role = Role.create(ROLE_ID, ROLE_NAME, AUTHORITIES);
        final RoleSnapshot roleSnapshot = role.getSnapshot();

        final Role anotherRole = Role.restore(roleSnapshot);
        final RoleSnapshot anotherRoleSnapshot = anotherRole.getSnapshot();

        assertEquals(roleSnapshot.id(), anotherRoleSnapshot.id());
        assertEquals(roleSnapshot.name(), anotherRoleSnapshot.name());
        assertEquals(roleSnapshot.authorities(), anotherRoleSnapshot.authorities());
        assertNotEquals(roleSnapshot.events().size(), anotherRoleSnapshot.events().size());
    }
}
