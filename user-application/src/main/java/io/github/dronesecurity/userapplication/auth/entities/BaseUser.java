/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.auth.entities;

import java.util.Objects;

/**
 * Implementation of {@link User}.
 */
class BaseUser implements User {

    private final String username;

    /**
     * Build the Base User.
     * @param username his username
     */
    protected BaseUser(final String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public final boolean equals(final Object o) {
        if (o == null) return false;
        if (this == o || this.getClass() == o.getClass()) return true;
        if (!User.class.isAssignableFrom(o.getClass())) return false;
        final BaseUser baseUser = (BaseUser) o;
        return this.getUsername().equals(baseUser.getUsername());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.getUsername());
    }
}
