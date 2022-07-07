/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.user;

import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Courier;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Maintainer;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.User;
import io.github.dronesecurity.userapplication.domain.user.entities.impl.CourierImpl;
import io.github.dronesecurity.userapplication.domain.user.entities.impl.MaintainerImpl;
import io.github.dronesecurity.userapplication.domain.user.exceptions.UsernameWithNumbersException;
import io.github.dronesecurity.userapplication.domain.user.objects.Username;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link User} entity and subclasses.
 */
final class UserAggregateTest {

    private static final List<String> DRONES = Arrays.asList("1", "2", "3");
    private static final String MAINTAINER_USERNAME_STRING = "maintainer";
    private static final Username MAINTAINER_USERNAME = Username.parse(MAINTAINER_USERNAME_STRING);
    private static final Maintainer MAINTAINER = new MaintainerImpl(MAINTAINER_USERNAME);
    private static final Username COURIER_USERNAME = Username.parse("courier");
    private static final Courier COURIER = new CourierImpl(COURIER_USERNAME, MAINTAINER.getUsername(), DRONES);

    @Test
    void testUsername() {
        assertThrowsExactly(UsernameWithNumbersException.class, () -> Username.parse("123"),
                "Username should not contain numbers.");

        assertTrue(Username.parse(MAINTAINER_USERNAME_STRING).isSameValueAs(MAINTAINER_USERNAME),
                "Username generated by the same String should be considered as the same value.");
        assertEquals(MAINTAINER_USERNAME_STRING, MAINTAINER_USERNAME.asString(),
                "Username as String should be equal to the String used to generate.");

        assertFalse(Username.parse("test").isSameValueAs(MAINTAINER_USERNAME),
                "Username generated by different String should be considered different values.");
    }

    @Test
    void testUserIdentity() {
        final Courier courier = new CourierImpl(COURIER_USERNAME, MAINTAINER_USERNAME, DRONES);

        assertTrue(courier.hasSameIdentityAs(COURIER),
                "User should have same identity as itself.");

        assertFalse(courier.hasSameIdentityAs(MAINTAINER),
                "Users with different usernames should be considered as different entities.");

        assertTrue(MAINTAINER_USERNAME.isSameValueAs(courier.supervisorUsername()),
                "Maintainer username should be the same used to build the entity.");

        assertEquals(DRONES.size(), courier.assignedDrones().size(),
                "Assigned drones should be the same.");

        courier.addDrone("4");
        assertEquals(DRONES.size() + 1, courier.assignedDrones().size(),
                "After adding a drone, assigned drones size should increase by 1.");

        courier.removeDrone("1");
        assertEquals(DRONES.size(), courier.assignedDrones().size(),
                "After removing a drone, assigned drones size should decrease by 1.");
    }

}
