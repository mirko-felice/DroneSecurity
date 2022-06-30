/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.exceptions;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.NegligenceReport;

/**
 * Exception thrown if {@link NegligenceReport} is building without providing data.
 */
public class ReportEmptyDataException extends IllegalArgumentException {

    private static final long serialVersionUID = 2L;

    /**
     * Build the exception.
     */
    public ReportEmptyDataException() {
        super("Can not close a report not providing data.");
    }
}
