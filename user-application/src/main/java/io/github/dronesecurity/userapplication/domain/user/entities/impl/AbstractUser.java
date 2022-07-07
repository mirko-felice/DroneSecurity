/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.user.entities.impl;

import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Role;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.User;
import io.github.dronesecurity.userapplication.domain.user.events.LoggedIn;
import io.github.dronesecurity.userapplication.domain.user.events.LoggedOut;
import io.github.dronesecurity.userapplication.domain.user.objects.Username;
import io.github.dronesecurity.userapplication.events.DomainEvents;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract base implementation of {@link User}.
 */
public abstract class AbstractUser implements User {

    private final Username username;
    private final Role role;
    private boolean logged;

    /**
     * Build the Base User.
     * @param username his {@link Username}
     * @param role his {@link Role}
     */
    protected AbstractUser(final Username username, final Role role) {
        this.username = username;
        this.role = role;
        this.logged = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Username getUsername() {
        return this.username;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Role getRole() {
        return this.role;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLogged() {
        return this.logged;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logIn() {
        this.logged = true;
        DomainEvents.raise(new LoggedIn(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logOut() {
        this.logged = false;
        DomainEvents.raise(new LoggedOut(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSameIdentityAs(final @NotNull User entity) {
        return this.username.isSameValueAs(entity.getUsername());
    }

}
