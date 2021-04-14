package users.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import users.user.events.UserCreatedEvent;
import users.user.events.UserEvent;
import users.user.events.UserInformationChangedEvent;
import users.user.vo.*;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final UUID ACCOUNT_ID = UUID.randomUUID();
    private final AccountId ACCOUNT = new AccountId(ACCOUNT_ID);

    private final Personality PERSONALITIES = new Personality("Firstname", "Surname");
    private final PhoneNumber PHONE_NUMBER = new PhoneNumber("48", "502964844");
    private final UserInfo USER_INFO = new UserInfo(PERSONALITIES, PHONE_NUMBER);

    private final Personality ANOTHER_PERSONALITIES = new Personality("Firstname1", "Surnmae");
    private final PhoneNumber ANOTHER_PHONE_NUMBER = new PhoneNumber("48", "502964843");
    private final UserInfo ANOTHER_USER_INFO = new UserInfo(ANOTHER_PERSONALITIES, ANOTHER_PHONE_NUMBER);

    private final UUID USER_ID = UUID.randomUUID();

    private final Class<UserCreatedEvent> USER_CREATED_EVENT = UserCreatedEvent.class;
    private final Class<UserInformationChangedEvent> USER_INFO_CHANGED_EVENT = UserInformationChangedEvent.class;

    @Test
    @DisplayName("User Creation Should Create UserCreateEvent Properly")
    void userCreationShouldCreateEvent() {
        final User user = User.create(USER_ID, ACCOUNT, USER_INFO);

        final Optional<UserEvent> userEvent = user.findLatestEvent();
        assertTrue(userEvent.isPresent());
        assertEquals(userEvent.get().getClass(), USER_CREATED_EVENT);

        final UserCreatedEvent userCreatedEvent = (UserCreatedEvent) userEvent.get();
        assertEquals(userCreatedEvent.aggregateId(), USER_ID);
        assertEquals(userCreatedEvent.account(), ACCOUNT);
        assertEquals(userCreatedEvent.userInfo(), USER_INFO);
    }

    @Test
    @DisplayName("Changing UserInfo Should Change User And Generate Event")
    void changingUserInfoShouldChangeUserAndGenerateEvent() {
        final User user = User.restore(USER_ID, ACCOUNT, USER_INFO);
        user.changeUserInformation(ANOTHER_USER_INFO);

        final Optional<UserEvent> userEvent = user.findLatestEvent();
        assertTrue(userEvent.isPresent());
        assertEquals(userEvent.get().getClass(), USER_INFO_CHANGED_EVENT);

        final UserInformationChangedEvent userInfoChangedEvent = (UserInformationChangedEvent) userEvent.get();
        assertEquals(userInfoChangedEvent.aggregateId(), USER_ID);
        assertEquals(userInfoChangedEvent.userInfo(), ANOTHER_USER_INFO);
    }

    @Test
    @DisplayName("Changing UserInfo With Same UserInfo Should Not Change User And Not Generate Event")
    void changingUserInfoWithSameUserInfoShouldNotChangeUser() {
        final User user = User.restore(USER_ID, ACCOUNT, USER_INFO);
        user.changeUserInformation(USER_INFO);

        final Optional<UserEvent> userEvent = user.findLatestEvent();
        assertFalse(userEvent.isPresent());
    }
}
