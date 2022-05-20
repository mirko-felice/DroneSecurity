/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.auth.entities;

/**
 * Implementation of {@link LoggedUser} extending {@link BaseUser}.
 */
class BaseLoggedUser extends BaseUser implements LoggedUser {

    private final Role role;

    /**
     * Build the User.
     * @param username his username
     * @param role his {@link Role}
     */
    protected BaseLoggedUser(final String username, final Role role) {
        super(username);
        this.role = role;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Role getRole() {
        return this.role;
    }

    @Override
    public String toString() {
        return this.getUsername();
    }
}
