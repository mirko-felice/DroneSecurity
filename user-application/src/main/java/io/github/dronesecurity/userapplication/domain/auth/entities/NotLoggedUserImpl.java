/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.auth.entities;

/**
 * Implementation of {@link NotLoggedUser} extending {@link BaseUser}.
 */
public class NotLoggedUserImpl extends BaseUser implements NotLoggedUser {

    private final String password;

    /**
     * Build the user.
     * @param username his username
     * @param password his password
     */
    public NotLoggedUserImpl(final String username, final String password) {
        super(username);
        this.password = password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return this.password;
    }

}
