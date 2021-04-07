package accounting.authority;

import accounting.authority.events.AuthorityCreatedEvent;
import accounting.authority.events.AuthorityEvent;
import accounting.authority.vo.AuthoritySnapshot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthorityTest {

    private final UUID DEFAULT_ID = UUID.randomUUID();
    private final String DEFAULT_NAME = "Authority Name";

    private final Class<AuthorityCreatedEvent> AUTHORITY_CREATED_EVENT = AuthorityCreatedEvent.class;

    @Test
    @DisplayName("Authority Should Return Snapshot With Proper Data")
    void authorityShouldReturnSnapshotWithProperData() {
        final Authority authority = Authority.create(DEFAULT_ID, DEFAULT_NAME);
        final AuthoritySnapshot authoritySnapshot = authority.getSnapshot();

        assertNotNull(authoritySnapshot);
        assertEquals(DEFAULT_ID, authoritySnapshot.id());
        assertEquals(DEFAULT_NAME, authoritySnapshot.name());
    }

    @Test
    @DisplayName("Creating Authority Should Generate Event")
    void authorityShouldReturnGenerateEvent() {
        final Authority authority = Authority.create(DEFAULT_ID, DEFAULT_NAME);
        final AuthoritySnapshot authoritySnapshot = authority.getSnapshot();
        final List<AuthorityEvent> authorityEvents = authoritySnapshot.events();
        assertNotNull(authorityEvents);
        assertFalse(authorityEvents.isEmpty());

        final Optional<AuthorityEvent> authorityEvent = authoritySnapshot.findLatestEvent();
        assertTrue(authorityEvent.isPresent());
        assertEquals(authorityEvent.get().getClass(), AUTHORITY_CREATED_EVENT);

        final AuthorityCreatedEvent authorityCreatedEvent = (AuthorityCreatedEvent) authorityEvent.get();
        assertEquals(authorityCreatedEvent.name(), DEFAULT_NAME);
        assertEquals(authorityCreatedEvent.aggregateId(), DEFAULT_ID);
    }

    @Test
    @DisplayName("Modifying Authority Snapshot Events Should Not Touch Authority Events")
    void modifyingSnapshotEventsShouldNotTouchAggregateEvents() {
        final Authority authority = Authority.create(DEFAULT_ID, DEFAULT_NAME);
        final List<AuthorityEvent> modifiedAuthorityEvents = authority.getSnapshot().events();
        modifiedAuthorityEvents.clear();

        final List<AuthorityEvent> originalAuthorityEvents = authority.getSnapshot().events();
        assertNotEquals(modifiedAuthorityEvents.size(), originalAuthorityEvents.size());
    }
}
