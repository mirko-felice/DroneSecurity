/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.user.events;

import io.github.dronesecurity.userapplication.domain.user.entities.contracts.User;
import io.github.dronesecurity.userapplication.events.DomainEvent;

/**
 * {@link DomainEvent} representing a {@link User} that has just logged into the system.
 */
public final class LoggedIn implements DomainEvent {

    private final User user;

    /**
     * Builds the event.
     * @param user {@link User} just logged in
     */
    public LoggedIn(final User user) {
        this.user = user;
    }

    /**
     * Gets the user just logged in.
     * @return the logged {@link User}
     */
    public User getUser() {
        return this.user;
    }
}
