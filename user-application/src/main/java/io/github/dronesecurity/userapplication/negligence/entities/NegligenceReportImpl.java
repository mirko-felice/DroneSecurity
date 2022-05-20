/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.negligence.entities;

import io.github.dronesecurity.userapplication.auth.entities.Courier;
import io.github.dronesecurity.userapplication.auth.entities.Maintainer;

/**
 * Base implementation of {@link NegligenceReport}.
 */
class NegligenceReportImpl implements NegligenceReport {

    private final String negligent;
    private final String assignee;
    private final DroneData data;

    /**
     * Build the report.
     * @param negligent the {@link Courier} that has committed negligence
     * @param assignee the {@link Maintainer} assigned to the report
     * @param data the {@link DroneData} associated to the report
     */
    NegligenceReportImpl(final String negligent, final String assignee, final DroneData data) {
        this.negligent = negligent;
        this.assignee = assignee;
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNegligent() {
        return this.negligent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DroneData getData() {
        return this.data.deepCopy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String assignedTo() {
        return this.assignee;
    }

}
