/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.entities;

import io.github.dronesecurity.userapplication.exceptions.ReportEmptyDataException;

import java.time.Instant;

/**
 * Implementation of {@link ClosedNegligenceReport}.
 */
class ClosedNegligenceReportImpl extends NegligenceReportWithIDImpl implements ClosedNegligenceReport {

    private final Instant closingInstant;
    private final NegligenceSolution solution;

    ClosedNegligenceReportImpl(final NegligenceReportWithID report,
                               final Instant closingInstant,
                               final NegligenceSolution solution) {
        super(report.getId(), report);
        if (report.getData().isEmpty())
            throw new ReportEmptyDataException();
        this.closingInstant = closingInstant;
        this.solution = solution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getClosingInstant() {
        return this.closingInstant;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NegligenceSolution getSolution() {
        return this.solution;
    }
}
