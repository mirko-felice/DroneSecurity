/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.user.exceptions;

import io.github.dronesecurity.userapplication.domain.user.entities.contracts.User;

/**
 * Exception thrown if {@link User} is NOT logged into the system.
 */
public class UserNotLoggedException extends IllegalStateException {

    private static final long serialVersionUID = 1L;

    /**
     * Build the exception.
     */
    public UserNotLoggedException() {
        super("User is NOT logged");
    }
}
