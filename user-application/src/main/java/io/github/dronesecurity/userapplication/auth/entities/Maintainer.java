/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.auth.entities;

import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.List;

/**
 * Entity representing the Maintainer user.
 */
public final class Maintainer extends BaseLoggedUser {

    private final List<String> couriers;

    /**
     * Build the Maintainer.
     * @param username his username
     * @param couriers {@link List} of all couriers supervised by the maintainer
     */
    public Maintainer(final String username, final Collection<String> couriers) {
        super(username, Role.MAINTAINER);
        this.couriers = List.copyOf(couriers);
    }

    /**
     * Gets the couriers supervised by this maintainer.
     * @return the couriers' usernames.
     */
    public @Unmodifiable List<String> getCouriers() {
        return List.copyOf(this.couriers);
    }
}
