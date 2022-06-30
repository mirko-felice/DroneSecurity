/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.entities;

import io.github.dronesecurity.userapplication.domain.auth.entities.Courier;
import io.github.dronesecurity.userapplication.domain.auth.entities.Maintainer;
import io.github.dronesecurity.userapplication.common.data.entities.DroneData;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;

import java.time.Instant;

/**
 * Base implementation of {@link NegligenceReport}.
 */
class NegligenceReportImpl implements NegligenceReport {

    private final String negligent;
    private final String assignee;
    private final DroneData data;
    private final long orderId;
    private final Instant negligenceInstant;

    /**
     * Build the report.
     * @param negligent the {@link Courier} that has committed negligence
     * @param assignee the {@link Maintainer} assigned to the report
     * @param data the {@link DroneData} associated to the report
     * @param orderId the {@link Order} identifier
     *                related to the negligence
     * @param negligenceInstant the {@link Instant} when the negligence has happened
     */
    NegligenceReportImpl(final String negligent,
                         final String assignee,
                         final DroneData data,
                         final long orderId,
                         final Instant negligenceInstant) {
        this.negligent = negligent;
        this.assignee = assignee;
        this.data = data;
        this.orderId = orderId;
        this.negligenceInstant = negligenceInstant;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public long getOrderId() {
        return this.orderId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getNegligenceInstant() {
        return this.negligenceInstant;
    }
}
