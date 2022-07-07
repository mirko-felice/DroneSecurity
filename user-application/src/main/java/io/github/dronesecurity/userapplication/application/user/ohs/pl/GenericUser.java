/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.user.ohs.pl;

/**
 * Public interface to decoupling internal implementation of
 * {@link io.github.dronesecurity.userapplication.domain.user.entities.contracts.User} to external usage.
 */
public class GenericUser {

    private final String username;
    private final UserRole userRole;

    /**
     * Build the user.
     * @param username his username
     * @param userRole his {@link UserRole}
     */
    public GenericUser(final String username, final UserRole userRole) {
        this.username = username;
        this.userRole = userRole;
    }

    /**
     * Gets his username.
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets his role.
     * @return his {@link UserRole}
     */
    public UserRole getRole() {
        return this.userRole;
    }
}
