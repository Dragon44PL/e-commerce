package accounting.authority;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
}
