/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.exceptions;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.ClosedNegligenceReport;

/**
 * Exception thrown if {@link ClosedNegligenceReport} is built using an invalid action form with closing instant before
 * detection instant.
 */
public final class InvalidClosingInstantException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Build the exception.
     */
    public InvalidClosingInstantException() {
        super("Closing instant of a report MUST NOT be before the detection instant!");
    }
}
