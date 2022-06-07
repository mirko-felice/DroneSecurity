/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.auth.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
        assertEquals(this.baseUser, notLoggedUser, SHOULD_BE_EQUALS);

        final User courier = new Courier(username, "supervisor", new ArrayList<>());
        assertEquals(courier, this.baseUser, SHOULD_BE_EQUALS);

        assertEquals(courier, notLoggedUser, SHOULD_BE_EQUALS);

        final User maintainer = new Maintainer(username, List.of(username));
        assertEquals(maintainer, this.baseUser, SHOULD_BE_EQUALS);

        assertEquals(maintainer, notLoggedUser, SHOULD_BE_EQUALS);
        assertEquals(maintainer, courier, SHOULD_BE_EQUALS);
    }

    @Test
    void testUsersDiversity() {
        final User notLoggedUser = new NotLoggedUserImpl("differentUsername", "password");
        assertNotEquals(notLoggedUser, this.baseUser, SHOULD_NOT_BE_EQUALS);

        final User courier = new Courier("courier", "supervisor", new ArrayList<>());
        assertNotEquals(courier, this.baseUser, SHOULD_NOT_BE_EQUALS);

        assertNotEquals(courier, notLoggedUser, SHOULD_NOT_BE_EQUALS);

        final User maintainer = new Maintainer("maintainer", List.of(courier.getUsername()));
        assertNotEquals(maintainer, this.baseUser, SHOULD_NOT_BE_EQUALS);

        assertNotEquals(maintainer, notLoggedUser, SHOULD_NOT_BE_EQUALS);
        assertNotEquals(maintainer, courier, SHOULD_NOT_BE_EQUALS);
    }

}
