/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.user.ohs.pl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Public interface to decoupling internal implementation of
 * {@link io.github.dronesecurity.userapplication.domain.user.entities.contracts.Courier} to external usage.
 */
public class Courier extends GenericUser {

    private final String supervisorUsername;
    private final List<String> assignedDrones;

    /**
     * Default constructor for json de/serialization.
     */
    public Courier() {
        super("", UserRole.COURIER);
        this.supervisorUsername = "";
        this.assignedDrones = new ArrayList<>();
    }

    /**
     * Build the courier.
     * @param username his username
     * @param supervisorUsername his supervisor username
     * @param assignedDrones his assigned drones
     */
    public Courier(final String username,
                   final String supervisorUsername,
                   final Collection<String> assignedDrones) {
        super(username, UserRole.COURIER);
        this.supervisorUsername = supervisorUsername;
        this.assignedDrones = new ArrayList<>(assignedDrones);
    }

    /**
     * Gets his supervisor username.
     * @return his supervisor username
     */
    public String getSupervisorUsername() {
        return this.supervisorUsername;
    }

    /**
     * Gets his assigned drones.
     * @return {@link List} of drone identifiers
     */
    public List<String> getAssignedDrones() {
        return List.copyOf(this.assignedDrones);
    }

}
