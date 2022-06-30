/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.entities;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Implementation of {@link OpenNegligenceReport}.
 */
class OpenNegligenceReportImpl extends NegligenceReportWithIDImpl implements OpenNegligenceReport {

    OpenNegligenceReportImpl(final NegligenceReportWithID report) {
        super(report.getId(), report);
    }

    @Contract("_, _ -> new")
    @Override
    public @NotNull ClosedNegligenceReport close(final Instant closingInstant, final NegligenceSolution solution) {
        return new ClosedNegligenceReportImpl(this, closingInstant, solution);
    }
}
