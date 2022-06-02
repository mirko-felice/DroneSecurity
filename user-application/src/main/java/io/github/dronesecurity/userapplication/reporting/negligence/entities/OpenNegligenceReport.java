/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.negligence.entities;

import io.github.dronesecurity.userapplication.exceptions.ReportEmptyDataException;

import java.time.Instant;

/**
 * Represents OPEN state of the {@link NegligenceReportWithID}.
 */
public interface OpenNegligenceReport extends NegligenceReportWithID {

    /**
     * Close this report.
     * @param closingInstant instant when the report has been closed
     * @return the closed report as a {@link ClosedNegligenceReport}
     * @throws ReportEmptyDataException if drone data of this report are empty
     */
    ClosedNegligenceReport close(Instant closingInstant);
}
