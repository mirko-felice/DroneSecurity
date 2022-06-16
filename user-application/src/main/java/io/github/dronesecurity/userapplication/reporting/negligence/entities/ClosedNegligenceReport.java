/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.negligence.entities;

import java.time.Instant;

/**
 * Represents CLOSED state of the {@link NegligenceReportWithID}.
 */
public interface ClosedNegligenceReport extends NegligenceReportWithID {

    /**
     * Gets the instant when the report has been closed.
     * @return the {@link Instant}
     */
    Instant getClosingInstant();

    /**
     * Gets the solution used to take action.
     * @return the solution text
     */
    String getSolution();
}
