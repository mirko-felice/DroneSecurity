/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.user.exceptions;

import io.github.dronesecurity.userapplication.domain.user.objects.Username;

/**
 * Exception thrown if {@link Username} is built assigning an arrival date before placing date.
 */
public final class UsernameWithNumbersException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Build the exception.
     */
    public UsernameWithNumbersException() {
        super("Username MUST NOT contain numbers!");
    }
}
