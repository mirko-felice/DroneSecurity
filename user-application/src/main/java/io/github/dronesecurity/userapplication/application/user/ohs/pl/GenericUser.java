/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.user.ohs.pl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Public interface to decoupling internal implementation of
 * {@link io.github.dronesecurity.userapplication.domain.user.entities.contracts.User} to external usage.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericUser {

    private final String username;
    private final UserRole role;

    /**
     * Default constructor for json de/serialization.
     */
    public GenericUser() {
        this.username = "";
        this.role = UserRole.NOT_LOGGED;
    }

    /**
     * Build the user.
     * @param username his username
     * @param role his {@link UserRole}
     */
    public GenericUser(final String username, final UserRole role) {
        this.username = username;
        this.role = role;
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
        return this.role;
    }
}
