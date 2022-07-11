/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.exceptions;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceIdentifier;

/**
 * Exception thrown if {@link NegligenceIdentifier} is built using a negative value.
 */
public final class NegligenceIdentifierCannotHaveNegativeValueException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Build the exception.
     */
    public NegligenceIdentifierCannotHaveNegativeValueException() {
        super("Negligence identifier MUST NOT have negative value!");
    }
}
