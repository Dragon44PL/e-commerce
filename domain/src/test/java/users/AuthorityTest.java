package users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import users.snapshots.AuthoritySnapshot;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthorityTest {

    private final UUID DEFAULT_ID = UUID.randomUUID();
    private final String DEFAULT_NAME = "Authority Name";

    @Test
    @DisplayName("Authority Should Return Snapshot With Proper Data")
    void authorityShouldReturnSnapshotWithProperData() {
        final Authority authority = new Authority(DEFAULT_ID, DEFAULT_NAME);
        final AuthoritySnapshot authoritySnapshot = authority.getSnapshot();

        assertNotNull(authoritySnapshot);
        assertEquals(DEFAULT_ID, authoritySnapshot.id());
        assertEquals(DEFAULT_NAME, authoritySnapshot.name());
    }

    @Test
    @DisplayName("Authority ID should be the same like the other one")
    void authorityIdShouldBeSame() {
        final Authority authority = new Authority(DEFAULT_ID, DEFAULT_NAME);
        final Authority another = new Authority(DEFAULT_ID, DEFAULT_NAME);

        assertTrue(authority.hasSameId(another));
        assertTrue(another.hasSameId(authority));
    }

    @Test
    @DisplayName("Authority ID Should Not Be The Same Like The Other One")
    void authorityIdShouldNotBeSame() {
        final Authority authority = new Authority(DEFAULT_ID, DEFAULT_NAME);
        final Authority another = new Authority(UUID.randomUUID(), DEFAULT_NAME);

        assertFalse(authority.hasSameId(another));
        assertFalse(another.hasSameId(authority));
    }
}
