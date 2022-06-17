/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.negligence.entities;

import io.github.dronesecurity.userapplication.common.data.entities.DroneDataImpl;

import java.util.Map;

/**
 * Implementation of {@link NegligenceDroneData}.
 */
public class NegligenceDroneDataImpl extends DroneDataImpl implements NegligenceDroneData {

    /**
     * Build the object starting from data.
     * @param proximity proximity detected
     * @param accelerometer accelerometer info detected
     * @param camera camera value detected
     */
    public NegligenceDroneDataImpl(final Double proximity,
                                   final Map<String, Integer> accelerometer,
                                   final Long camera) {
        super(proximity, accelerometer, camera);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NegligenceDroneData deepCopy() {
        return new NegligenceDroneDataImpl(this.getProximity(), this.getAccelerometer(), this.getCamera());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return this.getProximity() == null || this.getAccelerometer().isEmpty() || this.getCamera() == null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Sensor Data (click for details)";
    }
}
