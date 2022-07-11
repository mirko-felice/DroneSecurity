/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.exceptions;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Assignee;

/**
 * Exception thrown if {@link Assignee} is built using an empty username.
 */
public final class EmptyAssigneeException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Build the exception.
     */
    public EmptyAssigneeException() {
        super("Assignee MUST NOT be null or empty!");
    }
}
