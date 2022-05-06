package it.unibo.dronesecurity.userapplication.auth.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests {@link User} entity and subclasses.
 */
final class UserTest {

    private static final String SHOULD_BE_EQUALS =
            "Users should be equals if their usernames are the same.";
    private static final String SHOULD_NOT_BE_EQUALS =
            "Users should not be equals in case their username are not the same.";
    private User baseUser;

    @BeforeEach
    void setup() {
        this.baseUser = new BaseUser("username");
    }

    @Test
    void testUsersEquality() {
        final String username = "username";
        final User notLoggedUser = new NotLoggedUserImpl(username, "password");
        assertEquals(this.baseUser,notLoggedUser, SHOULD_BE_EQUALS);

        final User courier = new Courier(username, "supervisor");
        assertEquals(courier, this.baseUser, SHOULD_BE_EQUALS);

        assertEquals(courier, notLoggedUser, SHOULD_BE_EQUALS);

        final User maintainer = new Maintainer(username);
        assertEquals(maintainer, this.baseUser, SHOULD_BE_EQUALS);

        assertEquals(maintainer, notLoggedUser, SHOULD_BE_EQUALS);
        assertEquals(maintainer, courier, SHOULD_BE_EQUALS);
    }

    @Test
    void testUsersDiversity() {
        final User notLoggedUser = new NotLoggedUserImpl("differentUsername", "password");
        assertNotEquals(notLoggedUser, this.baseUser, SHOULD_NOT_BE_EQUALS);

        final User courier = new Courier("courier", "supervisor");
        assertNotEquals(courier, this.baseUser, SHOULD_NOT_BE_EQUALS);

        assertNotEquals(courier, notLoggedUser, SHOULD_NOT_BE_EQUALS);

        final User maintainer = new Maintainer("maintainer");
        assertNotEquals(maintainer, this.baseUser, SHOULD_NOT_BE_EQUALS);

        assertNotEquals(maintainer, notLoggedUser, SHOULD_NOT_BE_EQUALS);
        assertNotEquals(maintainer, courier, SHOULD_NOT_BE_EQUALS);
    }

}
