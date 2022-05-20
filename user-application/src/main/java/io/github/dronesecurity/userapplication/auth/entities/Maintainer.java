/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.auth.entities;

/**
 * Entity representing the Maintainer user.
 */
public final class Maintainer extends BaseLoggedUser {

    /**
     * Build the Maintainer.
     * @param username his username
     */
    public Maintainer(final String username) {
        super(username, Role.MAINTAINER);
    }

}
