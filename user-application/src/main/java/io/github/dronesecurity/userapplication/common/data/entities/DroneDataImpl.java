/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.common.data.entities;

import java.util.Map;

/**
 * Wrapper class for all the sensors' data detected.
 */
public class DroneDataImpl implements DroneData {

    private final Double proximity;
    private final Map<String, Integer> accelerometer;
    private final Long camera;

    /**
     * Build the object starting from data.
     * @param proximity proximity detected
     * @param accelerometer accelerometer info detected
     * @param camera camera value detected
     */
    public DroneDataImpl(final Double proximity, final Map<String, Integer> accelerometer, final Long camera) {
        this.proximity = proximity;
        this.accelerometer = Map.copyOf(accelerometer);
        this.camera = camera;
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
    public DroneData deepCopy() {
        return new DroneDataImpl(this.getProximity(), this.getAccelerometer(), this.getCamera());
    }
}
