/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.drone.monitoring.entities;

import java.time.Instant;
import java.util.Map;

/**
 * Implementation of {@link DroneData}.
 */
public class DroneDataImpl implements DroneData {

    private final Double proximity;
    private final Map<String, Integer> accelerometer;
    private final Long camera;
    private final Instant detectionInstant;
    private final long orderId;

    /**
     * Build the object starting from data.
     * @param proximity proximity detected
     * @param accelerometer accelerometer info detected
     * @param camera camera value detected
     * @param detectionInstant {@link Instant} when the detection is happened
     * @param orderId {@link io.github.dronesecurity.userapplication.shipping.courier.entities.Order} identifier related
     *                                                                                              to data detection
     */
    public DroneDataImpl(final Double proximity,
                         final Map<String, Integer> accelerometer,
                         final Long camera,
                         final Instant detectionInstant,
                         final long orderId) {
        this.proximity = proximity;
        this.accelerometer = Map.copyOf(accelerometer);
        this.camera = camera;
        this.detectionInstant = detectionInstant;
        this.orderId = orderId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getProximity() {
        return this.proximity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Integer> getAccelerometer() {
        return Map.copyOf(this.accelerometer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getCamera() {
        return this.camera;
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
