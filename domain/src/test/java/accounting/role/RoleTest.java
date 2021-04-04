package accounting.role;

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

    private final UUID ROLE_ID = UUID.randomUUID();
    private final String ROLE_NAME = "Role";

    private final UUID AUTHORITY_ID = UUID.randomUUID();
    private final Set<AuthorityId> AUTHORITIES = Set.of(new AuthorityId(AUTHORITY_ID));

    private final UUID ANOTHER_AUTHORITY_ID = UUID.randomUUID();
    private final AuthorityId ANOTHER_AUTHORITY = new AuthorityId(ANOTHER_AUTHORITY_ID);

    private final Class<AuthorityAddedEvent> AUTHORITY_ADDED_EVENT_CLAZZ = AuthorityAddedEvent.class;
    private final Class<AuthorityRemovedEvent> AUTHORITY_REMOVED_EVENT_CLAZZ = AuthorityRemovedEvent.class;

    @Test
    @DisplayName("Role Should Create Snapshot With Init Values")
    void roleShouldCreateSnapshotWithInitValues() {
        final Role role = new Role(ROLE_ID, ROLE_NAME, AUTHORITIES);
        final RoleSnapshot roleSnapshot = role.getSnapshot();

        assertNotNull(roleSnapshot);
        assertEquals(roleSnapshot.id().compareTo(ROLE_ID), 0);
        assertEquals(roleSnapshot.name(), ROLE_NAME);
        assertEquals(roleSnapshot.authorities().size(), AUTHORITIES.size());
    }

    @Test
    @DisplayName("Role Should Add Authority And Generates AuthorityAddedEvent")
    void roleShouldAddAuthorityAddGenerateEvent() {
        final Role role = new Role(ROLE_ID, ROLE_NAME, AUTHORITIES);
        final Optional<AuthorityAddedEvent> event = role.addAuthority(ANOTHER_AUTHORITY);
        final RoleSnapshot roleSnapshot = role.getSnapshot();

        assertTrue(event.isPresent());
        assertEquals(event.get().getClass(), AUTHORITY_ADDED_EVENT_CLAZZ);
        assertEquals(roleSnapshot.authorities().size(), 2);
    }

    @Test
    @DisplayName("Role Should Not Add Authority And Not Generate AuthorityAddedEvent")
    void roleShouldNotAddAuthorityAddNotGenerateEvent() {
        final Role role = new Role(ROLE_ID, ROLE_NAME, AUTHORITIES);
        role.addAuthority(ANOTHER_AUTHORITY);
        final Optional<AuthorityAddedEvent> anotherEvent = role.addAuthority(ANOTHER_AUTHORITY);
        final RoleSnapshot roleSnapshot = role.getSnapshot();

        assertFalse(anotherEvent.isPresent());
        assertEquals(roleSnapshot.authorities().size(), 2);
    }

    @Test
    @DisplayName("Role Should Not Add Authority When Another Has Same Id")
    void roleShouldNotAddAuthorityWhenNotSameId() {
        final Role role = new Role(ROLE_ID, ROLE_NAME, AUTHORITIES);
        final AuthorityId another = new AuthorityId(AUTHORITY_ID);
        final Optional<AuthorityAddedEvent> anotherEvent = role.addAuthority(another);
        final RoleSnapshot roleSnapshot = role.getSnapshot();

        assertFalse(anotherEvent.isPresent());
        assertEquals(roleSnapshot.authorities().size(), 1);
    }

    @Test
    @DisplayName("Role Should Remove Authority And  Generate AuthorityRemovedEvent")
    void roleShouldRemoveAuthorityAddGenerateEvent() {
        final Role role = new Role(ROLE_ID, ROLE_NAME, AUTHORITIES);
        role.addAuthority(ANOTHER_AUTHORITY);
        final Optional<AuthorityRemovedEvent> removedEvent = role.removeAuthority(ANOTHER_AUTHORITY);
        final RoleSnapshot roleSnapshot = role.getSnapshot();

        assertTrue(removedEvent.isPresent());
        assertEquals(removedEvent.get().getClass(), AUTHORITY_REMOVED_EVENT_CLAZZ);
        assertEquals(roleSnapshot.authorities().size(), 1);
    }

    @Test
    @DisplayName("Role Should Not Remove Authority And Not Generate AuthorityRemovedEvent")
    void roleShouldNotRemoveAuthorityAddNotGenerateEvent() {
        final Role role = new Role(ROLE_ID, ROLE_NAME, AUTHORITIES);
        final Optional<AuthorityRemovedEvent> removedEvent = role.removeAuthority(ANOTHER_AUTHORITY);
        final RoleSnapshot roleSnapshot = role.getSnapshot();

        assertFalse(removedEvent.isPresent());
        assertEquals(roleSnapshot.authorities().size(), 1);
    }
}
