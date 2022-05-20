/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.negligence.entities;

import io.github.dronesecurity.userapplication.exceptions.ReportEmptyDataException;

import java.time.Instant;

/**
 * Implementation of {@link ClosedNegligenceReport}.
 */
class ClosedNegligenceReportImpl extends NegligenceReportWithIDImpl implements ClosedNegligenceReport {

    private final Instant closingInstant;

    ClosedNegligenceReportImpl(final NegligenceReportWithID report, final Instant closingInstant) {
        super(report.getId(), report);
        if (report.getData().isEmpty())
            throw new ReportEmptyDataException();
        this.closingInstant = closingInstant;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getClosingInstant() {
        return this.closingInstant;
    }
}
