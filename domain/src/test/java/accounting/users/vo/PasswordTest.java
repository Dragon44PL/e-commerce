package accounting.users.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    private final String PASSWORD = "PASSWORD";
    private final Instant EXPIRED_AT = Instant.now();

    @Test
    @DisplayName("Password Should Not Be Expired")
    void passwordShouldBeExpired() {
        final Password password = new Password(PASSWORD, Instant.from(EXPIRED_AT.minus(10, ChronoUnit.SECONDS)));
        assertTrue(password.isExpired());
    }

    @Test
    @DisplayName("Password Should Not Be Expired")
    void passwordShouldBeNotExpired() {
        final Password password = new Password(PASSWORD, Instant.from(EXPIRED_AT.plus(10, ChronoUnit.SECONDS)));
        assertFalse(password.isExpired());
    }

    @Test
    @DisplayName("Password Value Should Be The Same")
    void passwordShouldBeTheSame() {
        final Password password = new Password(PASSWORD, EXPIRED_AT);
        assertTrue(password.same(PASSWORD));
    }

    @Test
    @DisplayName("Password Value Should Be Not The Same")
    void passwordShouldBeNotTheSame() {
        final Password password = new Password(PASSWORD, EXPIRED_AT);
        assertFalse(password.same("PASSWORd"));
    }

    @Test
    @DisplayName("Password Expiration Date Should Be Before Another Password Date")
    void passwordExpirationDateBeforeAnother() {
        final Password password = new Password(PASSWORD, EXPIRED_AT);
        final Password another = new Password(PASSWORD, EXPIRED_AT.plus(1, ChronoUnit.SECONDS));
        assertTrue(password.expireBefore(another));
    }

    @Test
    @DisplayName("Password Expiration Date Should Be After Another Password Date")
    void passwordExpirationDateAfterAnother() {
        final Password password = new Password(PASSWORD, EXPIRED_AT);
        final Password another = new Password(PASSWORD, EXPIRED_AT.minus(1, ChronoUnit.SECONDS));
        assertFalse(password.expireBefore(another));
    }

    @Test
    @DisplayName("Password should be a values copy of another")
    void passwordShouldBeValuesCopy() {
        final Password password = new Password(PASSWORD, EXPIRED_AT);
        final Password copy = Password.ofAnother(password);
        assertNotSame(copy.asString(), password.asString());
        assertEquals(copy.createdAt().compareTo(password.createdAt()), 0);
        assertEquals(copy.expireAt().compareTo(password.expireAt()), 0);
    }

    @Test
    @DisplayName("Password's Default Expiration Date Should Be Long.MAX_VALUE")
    void passwordExpirationDateMatchesLongMaxValue() {
        final Password password = new Password(PASSWORD);
        assertEquals(Long.MAX_VALUE, password.expireAt().toEpochMilli());
    }
}
