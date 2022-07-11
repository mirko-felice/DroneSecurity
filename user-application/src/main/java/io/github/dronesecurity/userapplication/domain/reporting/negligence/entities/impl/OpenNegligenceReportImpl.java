/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.impl;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.events.NegligenceReportClosed;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.*;
import io.github.dronesecurity.userapplication.events.DomainEvents;

/**
 * Implementation of {@link OpenNegligenceReport}.
 */
public final class OpenNegligenceReportImpl extends AbstractNegligenceReport implements OpenNegligenceReport {

    /**
     * Build the report.
     * @param id the {@link NegligenceIdentifier}
     * @param negligent the {@link Negligent} that has committed negligence
     * @param assignee the {@link Assignee} assigned to the report
     * @param data the {@link DroneData} associated to the report
     */
    public OpenNegligenceReportImpl(final NegligenceIdentifier id,
                                    final Negligent negligent,
                                    final Assignee assignee,
                                    final DroneData data) {
        super(id, negligent, assignee, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void takeAction(final NegligenceActionForm actionForm) {
        final ClosedNegligenceReport closedReport =
                new ClosedNegligenceReportImpl(
                        this.getId(),
                        this.getNegligent(),
                        this.assignedTo(),
                        this.getData(),
                        actionForm);
        DomainEvents.raise(new NegligenceReportClosed(closedReport));
    }
}
