/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.user;

import io.github.dronesecurity.userapplication.application.user.AuthenticationServiceImpl;
import io.github.dronesecurity.userapplication.application.user.UserManagerImpl;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Courier;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Maintainer;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Role;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.User;
import io.github.dronesecurity.userapplication.domain.user.objects.Username;
import io.github.dronesecurity.userapplication.domain.user.repo.UserRepository;
import io.github.dronesecurity.userapplication.domain.user.services.AuthenticationService;
import io.github.dronesecurity.userapplication.domain.user.services.UserManager;
import io.github.dronesecurity.userapplication.exceptions.UserAlreadyLoggedException;
import io.github.dronesecurity.userapplication.exceptions.UserNotLoggedException;
import io.github.dronesecurity.userapplication.infrastructure.user.repo.InMemoryUserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test business logic related to {@link User}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
final class BusinessTest {

    private static final int FIRST = 1;
    private static final int SECOND = 2;
    private static final int THIRD = 3;
    private static final int FOURTH = 4;
    private static final int FIFTH = 5;
    private static final int SIXTH = 6;
    private static final UserRepository USER_REPOSITORY = new InMemoryUserRepository();
    private static final AuthenticationService AUTHENTICATION_SERVICE = new AuthenticationServiceImpl(USER_REPOSITORY);
    private static final UserManager USER_MANAGER = new UserManagerImpl(USER_REPOSITORY);
    private static final String COURIER_USERNAME = "courier";

    @Test
    @org.junit.jupiter.api.Order(FIRST)
    void testLoginCourier() {
        final Username username = Username.parse(COURIER_USERNAME);
        final String correctPassword = "courierPassword";
        assertTrue(AUTHENTICATION_SERVICE.logIn(username, correctPassword),
                "Logging in with correct credentials should return true.");

        final Optional<Role> loggedUserRole = USER_MANAGER.checkLoggedUserRole();
        assertTrue(loggedUserRole.isPresent(),
                "After logging in, logged user role should be present.");
        assertEquals(Role.COURIER, loggedUserRole.get(),
                "After logging in as a Courier, logged user role should be COURIER.");

        final Optional<Courier> loggedCourier = USER_MANAGER.retrieveLoggedCourierIfPresent();
        assertTrue(loggedCourier.isPresent(),
                "After logging in as a Courier, logged courier should be present.");
        assertTrue(loggedCourier.get().isLogged(),
                "After logging in, courier should be logged.");
        assertTrue(username.isSameValueAs(loggedCourier.get().getUsername()),
                "Username of logged user should be the same as the username used to log in.");
    }

    @Test
    @org.junit.jupiter.api.Order(SECOND)
    void testLogout() {
        assertTrue(AUTHENTICATION_SERVICE.logOut(),
                "Logging out after logging in should return true.");
        assertThrowsExactly(UserNotLoggedException.class, AUTHENTICATION_SERVICE::logOut,
                "After logging out, logging out again should throw UserNotLoggedException.");

        final Optional<Role> loggedUserRole = USER_MANAGER.checkLoggedUserRole();
        assertTrue(loggedUserRole.isEmpty(),
                "After logging out, logged user role should be empty.");
    }

    @Test
    @org.junit.jupiter.api.Order(THIRD)
    void testLoginMaintainer() {
        final Username username = Username.parse("maintainer");
        final String password = "maintainerPassword";
        assertTrue(AUTHENTICATION_SERVICE.logIn(username, password),
                "Logging in with correct credentials should return true.");

        final Optional<Role> loggedUserRole = USER_MANAGER.checkLoggedUserRole();
        assertTrue(loggedUserRole.isPresent(),
                "After logging in, logged user role should be present.");
        assertEquals(Role.MAINTAINER, loggedUserRole.get(),
                "After logging in as a Maintainer, logged user role should be MAINTAINER.");

        final Optional<Maintainer> loggedMaintainer = USER_MANAGER.retrieveLoggedMaintainerIfPresent();
        assertTrue(loggedMaintainer.isPresent(),
                "After logging in as a Maintainer, logged maintainer should be present.");
        assertTrue(loggedMaintainer.get().isLogged(),
                "After logging in, maintainer should be logged.");
        assertTrue(username.isSameValueAs(loggedMaintainer.get().getUsername()),
                "Username of logged user should be the same as the username used to log in.");
    }

    @Test
    @org.junit.jupiter.api.Order(FOURTH)
    void testWrongLogin() {
        AUTHENTICATION_SERVICE.logOut();
        final Username username = Username.parse(COURIER_USERNAME);
        final String wrongPassword = "wrongPassword";
        assertFalse(AUTHENTICATION_SERVICE.logIn(username, wrongPassword),
                "Logging in with wrong credentials should return false.");

        final String correctPassword = "courierPassword";
        AUTHENTICATION_SERVICE.logIn(username, correctPassword);

        assertThrowsExactly(UserAlreadyLoggedException.class,
                () -> AUTHENTICATION_SERVICE.logIn(username, correctPassword),
                "After logging in, logging in again should throw UserAlreadyLoggedException.");
    }

    @Test
    @org.junit.jupiter.api.Order(FIFTH)
    void testRetrievingCourier() {
        final Username wrongUsername = Username.parse("wrong");
        final Optional<Courier> emptyCourier = USER_MANAGER.retrieveCourierByUsername(wrongUsername);
        assertTrue(emptyCourier.isEmpty(),
                "Retrieving courier that does not exist should be empty.");

        final Username correctUsername = Username.parse(COURIER_USERNAME);
        final Optional<Courier> courier = USER_MANAGER.retrieveCourierByUsername(correctUsername);
        assertTrue(courier.isPresent(),
                "Retrieving courier that does exist should be present.");
    }

    @Test
    @org.junit.jupiter.api.Order(SIXTH)
    void testRetrievingMaintainer() {
        final Username wrongUsername = Username.parse("wrong");
        final Optional<Maintainer> emptyMaintainer = USER_MANAGER.retrieveMaintainerByUsername(wrongUsername);
        assertFalse(emptyMaintainer.isPresent(),
                "Retrieving maintainer that does not exist should be empty.");

        final Username correctUsername = Username.parse("maintainer");
        final Optional<Maintainer> maintainer = USER_MANAGER.retrieveMaintainerByUsername(correctUsername);
        assertTrue(maintainer.isPresent(),
                "Retrieving maintainer that does exist should be present.");
    }
}
