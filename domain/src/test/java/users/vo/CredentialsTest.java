package users.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

public class CredentialsTest {

    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD_VALUE = "PASSWORD";
    private static final Instant EXPIRES_AT = Instant.now();
    private static final Password PASSWORD = new Password(PASSWORD_VALUE, EXPIRES_AT);

    private static final String CHANGED_USERNAME = "CHANGED_USERNAME";
    private static final String CHANGED_PASSWORD_VALUE = "CHANGED_PASSWORD";
    private static final Instant CHANGED_EXPIRES_AT = Instant.now().plus(60, ChronoUnit.SECONDS);
    private static final Password CHANGED_PASSWORD = new Password(CHANGED_PASSWORD_VALUE, CHANGED_EXPIRES_AT);

    @Test
    @DisplayName("Credentials Username Value Should Be The Same")
    void credentialsUsernameValueShouldBeTheSame() {
        final Credentials credentials = new Credentials(USERNAME, PASSWORD);
        assertEquals(USERNAME, credentials.getUsername());
    }

    @Test
    @DisplayName("Credentials Password Values Should Be The Same")
    void credentialsPasswordValuesShouldBeTheSame() {
        final Credentials credentials = new Credentials(USERNAME, PASSWORD);
        assertEquals(PASSWORD.asString(), credentials.getPassword().asString());
        assertEquals(PASSWORD.createdAt().toEpochMilli(), credentials.getPassword().createdAt().toEpochMilli());
        assertEquals(PASSWORD.expireAt().toEpochMilli(), credentials.getPassword().expireAt().toEpochMilli());
    }

    @Test
    @DisplayName("Changing Credentials Username Should Be The Same As Origin Value")
    void credentialsChangedUsernameValueShouldBeTheSame() {
        final Credentials credentials = new Credentials(USERNAME, PASSWORD);
        final Credentials changedCredentials = credentials.changeUsername(CHANGED_USERNAME);
        assertEquals(changedCredentials.getUsername(), CHANGED_USERNAME);
        assertEquals(credentials.getUsername(), USERNAME);
    }

    @Test
    @DisplayName("Credentials Password Values Should Be The Same")
    void credentialsChangedUsernameNotChangePassword() {
        final Credentials credentials = new Credentials(USERNAME, PASSWORD);
        final Credentials changedCredentials = credentials.changeUsername(CHANGED_USERNAME);

        assertEquals(PASSWORD.asString(), changedCredentials.getPassword().asString());
        assertEquals(PASSWORD.createdAt().toEpochMilli(), changedCredentials.getPassword().createdAt().toEpochMilli());
        assertEquals(PASSWORD.expireAt().toEpochMilli(), changedCredentials.getPassword().expireAt().toEpochMilli());
    }

    @Test
    @DisplayName("Changing Credentials Username Should Be The Same As Origin Value")
    void credentialsChangedPasswordValuesShouldBeTheSame() {
        final Credentials credentials = new Credentials(USERNAME, PASSWORD);
        final Credentials changedCredentials = credentials.changePassword(CHANGED_PASSWORD);

        assertEquals(CHANGED_PASSWORD.asString(), changedCredentials.getPassword().asString());
        assertEquals(CHANGED_PASSWORD.createdAt().toEpochMilli(), changedCredentials.getPassword().createdAt().toEpochMilli());
        assertEquals(CHANGED_PASSWORD.expireAt().toEpochMilli(), changedCredentials.getPassword().expireAt().toEpochMilli());

        assertEquals(PASSWORD.asString(), credentials.getPassword().asString());
        assertEquals(PASSWORD.createdAt().toEpochMilli(), credentials.getPassword().createdAt().toEpochMilli());
        assertEquals(PASSWORD.expireAt().toEpochMilli(), credentials.getPassword().expireAt().toEpochMilli());
    }

    @Test
    @DisplayName("Credentials Password Values Should Be The Same")
    void credentialsChangedPasswordNotChangeUsername() {
        final Credentials credentials = new Credentials(USERNAME, PASSWORD);
        final Credentials changedCredentials = credentials.changePassword(CHANGED_PASSWORD);

        assertEquals(USERNAME, credentials.getUsername());
        assertEquals(USERNAME, changedCredentials.getUsername());
    }

    @Test
    @DisplayName("Creating Credentials Of Another Credentials Doesn't Change Values")
    void credentialsOfAnotherCredentialsNotChangeValues() {
        final Credentials credentials = new Credentials(USERNAME, PASSWORD);
        final Credentials another = Credentials.ofCredentials(credentials);

        assertEquals(USERNAME, another.getUsername());
        assertEquals(PASSWORD.asString(), another.getPassword().asString());
        assertEquals(PASSWORD.createdAt().toEpochMilli(), another.getPassword().createdAt().toEpochMilli());
        assertEquals(PASSWORD.expireAt().toEpochMilli(), another.getPassword().expireAt().toEpochMilli());
    }


}
