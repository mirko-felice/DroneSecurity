/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.auth.entities;

import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Entity representing the Courier user, extending {@link BaseLoggedUser}.
 */
public final class Courier extends BaseLoggedUser {

    private final String supervisor;
    private final List<String> drones;

    /**
     * Build the Courier.
     * @param username his username
     * @param supervisor his supervisor
     * @param drones {@link Collection} containing the drone identifiers available for the courier
     */
    public Courier(final String username, final String supervisor, final Collection<String> drones) {
        super(username, Role.COURIER);
        this.supervisor = supervisor;
        this.drones = new ArrayList<>(drones);
    }

    /**
     * Gets his supervisor as a {@link String}.
     * @return the maintainer
     */
    public String getSupervisor() {
        return this.supervisor;
    }

    /**
     * Gets all the drone identifiers available for the courier.
     * @return the {@link List} of drone identifiers.
     */
    public @Unmodifiable List<String> getDrones() {
        return List.copyOf(this.drones);
    }

    /**
     * Removes a drone only temporarily (not in Database).
     * @param droneId drone identifier to remove
     */
    public void removeDrone(final String droneId) {
        this.drones.remove(droneId);
    }

    /**
     * Adds a drone only temporarily (not in Database).
     * @param droneId drone identifier to add
     */
    public void addDrone(final String droneId) {
        this.drones.add(droneId);
    }
}
