/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.user.ohs.pl;

/**
 * Public interface to decoupling internal implementation of
 * {@link io.github.dronesecurity.userapplication.domain.user.entities.contracts.Maintainer} to external usage.
 */
public class Maintainer extends GenericUser {

    /**
     * Build the maintainer.
     * @param username his username
     */
    public Maintainer(final String username) {
        super(username, UserRole.MAINTAINER);
    }

}
