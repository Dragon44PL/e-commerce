package users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import users.events.role.AuthorityAddedEvent;
import users.events.role.AuthorityRemovedEvent;
import users.snapshots.RoleSnapshot;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    private final UUID ROLE_ID = UUID.randomUUID();
    private final String ROLE_NAME = "Role";

    private final UUID AUTHORITY_ID = UUID.randomUUID();
    private final String AUTHORITY_NAME = "Another name";
    private final Set<Authority> AUTHORITIES = Set.of(new Authority(AUTHORITY_ID, AUTHORITY_NAME));

    private final UUID ANOTHER_AUTHORITY_ID = UUID.randomUUID();
    private final String ANOTHER_AUTHORITY_NAME = "Another name";
    private final Authority ANOTHER_AUTHORITY = new Authority(ANOTHER_AUTHORITY_ID, ANOTHER_AUTHORITY_NAME);

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
        final Authority another = new Authority(AUTHORITY_ID, ANOTHER_AUTHORITY_NAME);
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

    @Test
    @DisplayName("Role ID Should Be The Same Like The Other One")
    void roleIdShouldBeSame() {
        final Role role = new Role(ROLE_ID, ROLE_NAME, new HashSet<>());
        final Role another = new Role(ROLE_ID, ROLE_NAME, new HashSet<>());

        assertTrue(role.hasSameId(another));
        assertTrue(another.hasSameId(role));
    }

    @Test
    @DisplayName("Role ID Should Not Be The Same Like The Other One")
    void roleIdShouldNotBeSame() {
        final Role role = new Role(ROLE_ID, ROLE_NAME, new HashSet<>());
        final Role another = new Role(UUID.randomUUID(), ROLE_NAME, new HashSet<>());

        assertFalse(role.hasSameId(another));
        assertFalse(another.hasSameId(role));
    }
}
