/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.exceptions;

/**
 * Exception thrown if user is already logged into the system.
 */
public class UserAlreadyLoggedException extends IllegalStateException {

    private static final long serialVersionUID = 1L;

    /**
     * Build the exception.
     */
    public UserAlreadyLoggedException() {
        super("User is already logged in!");
    }
}
