package org.ecommerce.shop.accounting.authority;

import org.ecommerce.shop.accounting.authority.events.AuthorityCreatedEvent;
import org.ecommerce.shop.accounting.authority.events.AuthorityEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthorityTest {

    private final UUID DEFAULT_ID = UUID.randomUUID();
    private final String DEFAULT_NAME = "Authority Name";

    private final Class<AuthorityCreatedEvent> AUTHORITY_CREATED_EVENT = AuthorityCreatedEvent.class;

    @Test
    @DisplayName("Creating Authority Should Generate Event")
    void authorityShouldReturnGenerateEvent() {
        final Authority authority = Authority.create(DEFAULT_ID, DEFAULT_NAME);

        final Optional<AuthorityEvent> authorityEvent = authority.findLatestEvent();
        assertTrue(authorityEvent.isPresent());
        assertEquals(authorityEvent.get().getClass(), AUTHORITY_CREATED_EVENT);

        final AuthorityCreatedEvent authorityCreatedEvent = (AuthorityCreatedEvent) authorityEvent.get();
        assertEquals(authorityCreatedEvent.name(), DEFAULT_NAME);
        assertEquals(authorityCreatedEvent.aggregateId(), DEFAULT_ID);
    }

    @Test
    @DisplayName("Restoring Authority Should Create Authority And Not Generate Event")
    void restoringShouldCreateAuthorityAndNotGenerateEvent() {
        final Authority authority = Authority.restore(DEFAULT_ID, DEFAULT_NAME);

        final Optional<AuthorityEvent> authorityEvent = authority.findLatestEvent();
        assertFalse(authorityEvent.isPresent());
    }
}
