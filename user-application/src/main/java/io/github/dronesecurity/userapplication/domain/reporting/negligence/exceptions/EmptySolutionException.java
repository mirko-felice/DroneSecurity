/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.exceptions;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceActionForm;

/**
 * Exception thrown if {@link NegligenceActionForm} is built using an empty solution.
 */
public final class EmptySolutionException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Build the exception.
     */
    public EmptySolutionException() {
        super("Negligence solution MUST NOT be null or empty!");
    }
}
