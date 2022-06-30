/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.monitoring.entities;

import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.common.data.entities.DroneDataImpl;

import java.time.Instant;
import java.util.Map;

/**
 * Implementation of {@link MonitoringDroneData}.
 */
public class MonitoringDroneDataImpl extends DroneDataImpl implements MonitoringDroneData {

    private final Instant detectionInstant;
    private final long orderId;

    /**
     * Build the object starting from data.
     * @param proximity proximity detected
     * @param accelerometer accelerometer info detected
     * @param camera camera value detected
     * @param detectionInstant {@link Instant} when the detection is happened
     * @param orderId {@link Order} identifier related to data detection
     */
    public MonitoringDroneDataImpl(final Double proximity,
                                   final Map<String, Integer> accelerometer,
                                   final Long camera,
                                   final Instant detectionInstant,
                                   final long orderId) {
        super(proximity, accelerometer, camera);
        this.detectionInstant = detectionInstant;
        this.orderId = orderId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getDetectionInstant() {
        return this.detectionInstant;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getOrderId() {
        return this.orderId;
    }
}
