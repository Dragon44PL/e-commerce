package users.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import users.user.User;
import users.user.events.UserCreatedEvent;
import users.user.events.UserEvent;
import users.user.events.UserInformationChangedEvent;
import users.user.vo.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final UUID ACCOUNT_ID = UUID.randomUUID();
    private final AccountId ACCOUNT = new AccountId(ACCOUNT_ID);

    private final Personality PERSONALITIES = new Personality("Firstname", "Surnmae");
    private final PhoneNumber PHONE_NUMBER = new PhoneNumber("48", "502964844");
    private final UserInfo USER_INFO = new UserInfo(PERSONALITIES, PHONE_NUMBER);

    private final Personality ANOTHER_PERSONALITIES = new Personality("Firstname", "Surnmae");
    private final PhoneNumber ANOTHER_PHONE_NUMBER = new PhoneNumber("48", "502964844");
    private final UserInfo ANOTHER_USER_INFO = new UserInfo(ANOTHER_PERSONALITIES, ANOTHER_PHONE_NUMBER);

    private final UUID USER_ID = UUID.randomUUID();

    private final Class<UserCreatedEvent> USER_CREATED_EVENT = UserCreatedEvent.class;
    private final Class<UserInformationChangedEvent> USER_INFO_CHANGED_EVENT = UserInformationChangedEvent.class;

    @Test
    @DisplayName("User Creation Should Create UserSnapshot Properly")
    void userCreationShouldCreateProperly() {
        final User user = User.create(USER_ID, ACCOUNT, USER_INFO);
        final UserSnapshot userSnapshot = user.getSnapshot();

        assertEquals(userSnapshot.id(), USER_ID);
        assertEquals(userSnapshot.account(), ACCOUNT);
        assertEquals(userSnapshot.userInfo(), USER_INFO);
    }

    @Test
    @DisplayName("User Creation Should Create UserCreateEvent Properly")
    void userCreationShouldCreateEvent() {
        final User user = User.create(USER_ID, ACCOUNT, USER_INFO);
        final UserSnapshot userSnapshot = user.getSnapshot();
        final Optional<UserEvent> userEvent = userSnapshot.findLatestEvent();
        assertNotNull(userEvent);
        assertTrue(userEvent.isPresent());
        assertEquals(userEvent.get().getClass(), USER_CREATED_EVENT);

        final UserCreatedEvent userCreatedEvent = (UserCreatedEvent) userEvent.get();
        assertEquals(userCreatedEvent.aggregateId(), USER_ID);
        assertEquals(userCreatedEvent.account(), ACCOUNT);
        assertEquals(userCreatedEvent.userInfo(), USER_INFO);
    }

    @Test
    @DisplayName("Modifying Category Snapshot Events Should Not Touch Category Events")
    void modifyingSnapshotEventsShouldNotTouchAggregateEvents() {
        final User user = User.create(USER_ID, ACCOUNT, USER_INFO);

        final List<UserEvent> modifiedUserEvents = user.getSnapshot().events();
        modifiedUserEvents.clear();

        final List<UserEvent> originalUserEvents = user.getSnapshot().events();
        assertNotEquals(modifiedUserEvents.size(), originalUserEvents.size());
    }

    @Test
    @DisplayName("Changing UserInfo Should Change User And Generate Event")
    void changingUserInfoShoulChangeUserAndGenerateEvent() {
        final User user = User.create(USER_ID, ACCOUNT, USER_INFO);
        user.changeUserInformation(ANOTHER_USER_INFO);
        final UserSnapshot userSnapshot = user.getSnapshot();

        final Optional<UserEvent> userEvent = userSnapshot.findLatestEvent();
        assertTrue(userEvent.isPresent());
        assertEquals(userEvent.get().getClass(), USER_INFO_CHANGED_EVENT);

        final UserInformationChangedEvent userInfoChangedEvent = (UserInformationChangedEvent) userEvent.get();
        assertEquals(userInfoChangedEvent.aggregateId(), USER_ID);
        assertEquals(userInfoChangedEvent.userInfo(), ANOTHER_USER_INFO);
        assertEquals(userSnapshot.userInfo(), ANOTHER_USER_INFO);
    }

    @Test
    @DisplayName("Restoring User Should Create User With Values From User Snapshot")
    void restoringShouldCreateCategoryWithValuesFromSnapshot() {
        final User user = User.create(USER_ID, ACCOUNT, USER_INFO);
        final UserSnapshot userSnapshot = user.getSnapshot();

        final User anotherUser = User.restore(userSnapshot);
        final UserSnapshot anotherUserSnapshot = anotherUser.getSnapshot();

        assertEquals(userSnapshot.id(), anotherUserSnapshot.id());
        assertEquals(userSnapshot.account(), anotherUserSnapshot.account());
        assertEquals(userSnapshot.userInfo(), anotherUserSnapshot.userInfo());
        assertNotEquals(userSnapshot.events().size(), anotherUserSnapshot.events().size());
    }
}
