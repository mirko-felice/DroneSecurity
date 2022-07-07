/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.user.entities.impl;

import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Courier;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Role;
import io.github.dronesecurity.userapplication.domain.user.objects.Username;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@link Courier}.
 */
public final class CourierImpl extends AbstractUser implements Courier {

    private final Username supervisor;
    private final List<String> drones;

    /**
     * Build the Courier.
     * @param username his {@link Username}
     * @param supervisor his supervisor {@link Username}
     * @param drones {@link Collection} containing the drone identifiers available for the courier
     */
    public CourierImpl(final Username username, final Username supervisor, final Collection<String> drones) {
        super(username, Role.COURIER);
        this.supervisor = supervisor;
        this.drones = new ArrayList<>(drones);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Username supervisorUsername() {
        return this.supervisor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Unmodifiable List<String> assignedDrones() {
        return List.copyOf(this.drones);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeDrone(final String droneId) {
        this.drones.remove(droneId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDrone(final String droneId) {
        this.drones.add(droneId);
    }
}
