/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.exceptions;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Negligent;

/**
 * Exception thrown if {@link Negligent} is built using an empty username.
 */
public final class EmptyNegligentException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Build the exception.
     */
    public EmptyNegligentException() {
        super("Negligent MUST NOT be null or empty!");
    }
}
