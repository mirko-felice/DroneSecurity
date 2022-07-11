/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.events;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.events.DomainEvent;

/**
 * {@link DomainEvent} representing a {@link ClosedNegligenceReport} that has been closed.
 */
public final class NegligenceReportClosed implements DomainEvent {

    private final ClosedNegligenceReport report;

    /**
     * Builds the event.
     * @param report {@link ClosedNegligenceReport} that has been closed
     */
    public NegligenceReportClosed(final ClosedNegligenceReport report) {
        this.report = report;
    }

    /**
     * Gets the report that has been closed.
     * @return the {@link ClosedNegligenceReport}
     */
    public ClosedNegligenceReport getReport() {
        return this.report;
    }
}
