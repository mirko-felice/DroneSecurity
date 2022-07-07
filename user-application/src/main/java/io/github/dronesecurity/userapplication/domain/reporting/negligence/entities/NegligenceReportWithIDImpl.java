/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.entities;

import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link NegligenceReportWithID}.
 */
class NegligenceReportWithIDImpl extends NegligenceReportImpl implements NegligenceReportWithID {

    private final long id;

    /* default */ NegligenceReportWithIDImpl(final long id, final @NotNull NegligenceReport report) {
        super(report.getNegligent(), report.assignedTo(), report.getData(), report.getOrderId(),
                report.getNegligenceInstant());
        this.id = id;
    }

    @Override
    public long getId() {
        return this.id;
    }
}
