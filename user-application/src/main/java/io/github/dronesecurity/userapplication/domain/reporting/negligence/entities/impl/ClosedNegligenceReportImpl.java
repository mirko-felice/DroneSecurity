/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.impl;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.exceptions.InvalidClosingInstantException;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.*;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link ClosedNegligenceReport}.
 */
public final class ClosedNegligenceReportImpl extends AbstractNegligenceReport implements ClosedNegligenceReport {

    private final NegligenceActionForm actionForm;

    /**
     * Build the report.
     * @param id the {@link NegligenceIdentifier}
     * @param negligent the {@link Negligent} that has committed negligence
     * @param assignee the {@link Assignee} assigned to the report
     * @param data the {@link DroneData} associated to the report
     * @param actionForm the {@link NegligenceActionForm} used to close the report
     * @throws InvalidClosingInstantException if {@code actionForm} has closing instant before detection instant
     */
    // TODO search all   throw new
    public ClosedNegligenceReportImpl(final NegligenceIdentifier id,
                                      final Negligent negligent,
                                      final Assignee assignee,
                                      final DroneData data,
                                      final @NotNull NegligenceActionForm actionForm) {
        super(id, negligent, assignee, data);
        if (actionForm.getClosingInstant().isBefore(data.detectionInstantAsDate()))
            throw new InvalidClosingInstantException();
        this.actionForm = actionForm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NegligenceActionForm getActionForm() {
        return this.actionForm;
    }
}
