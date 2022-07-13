/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.user.entities.contracts;

import io.github.dronesecurity.userapplication.domain.user.objects.Username;

import java.util.List;

/**
 * Represents a particular {@link User} with the {@link Role} of {@link Role#COURIER}.
 */
public interface Courier extends User {

    /**
     * Gets his supervisor {@link Username}.
     * @return the {@link Username}
     */
    Username supervisorUsername();

    /**
     * Gets all the drone identifiers available for the courier.
     * @return the {@link List} of drone identifiers.
     */
    List<String> assignedDrones();

    /**
     * Removes a drone only temporarily (not in Database).
     * @param droneId drone identifier to remove
     */
    void removeDrone(String droneId);

    /**
     * Adds a drone only temporarily (not in Database).
     * @param droneId drone identifier to add
     */
    void addDrone(String droneId);
}
