/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.impl;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Assignee;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.DroneData;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceIdentifier;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Negligent;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract implementation of {@link NegligenceReport}.
 */
public abstract class AbstractNegligenceReport implements NegligenceReport {

    private final NegligenceIdentifier id;
    private final Negligent negligent;
    private final Assignee assignee;
    private final DroneData data;

    /**
     * Build the report.
     * @param id the {@link NegligenceIdentifier}
     * @param negligent the {@link Negligent} that has committed negligence
     * @param assignee the {@link Assignee} assigned to the report
     * @param data the {@link DroneData} associated to the report
     */
    protected AbstractNegligenceReport(final NegligenceIdentifier id,
                                       final Negligent negligent,
                                       final Assignee assignee,
                                       final DroneData data) {
        this.id = id;
        this.negligent = negligent;
        this.assignee = assignee;
        this.data = data;
        // TODO validation?
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NegligenceIdentifier getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Negligent getNegligent() {
        return this.negligent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Assignee assignedTo() {
        return this.assignee;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DroneData getData() {
        return this.data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSameIdentityAs(final @NotNull NegligenceReport entity) {
        return this.id.isSameValueAs(entity.getId());
    }
}
