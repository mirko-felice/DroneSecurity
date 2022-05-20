/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.events;

import java.util.Map;

/**
 * The event to be raised when the drone publishes its sensor data.
 */
public class DataRead implements Event {
    private final double proximity;
    private final Map<String, Double> accelerometerData;
    private final double cameraData;

    /**
     * Instantiates the Data Read event.
     *
     * @param proximity data read by the proximity sensor
     * @param accelerometer data read by the accelerometer
     * @param camera data read by the camera
     */
    public DataRead(final double proximity, final Map<String, Double> accelerometer, final double camera) {
        this.proximity = proximity;
        this.accelerometerData = Map.copyOf(accelerometer);
        this.cameraData = camera;
    }

    /**
     * Gets proximity sensor data.
     *
     * @return value read by the proximity sensor
     */
    public double getProximity() {
        return this.proximity;
    }

    /**
     * Gets accelerometer data.
     *
     * @return value read by the accelerometer
     */
    public Map<String, Double> getAccelerometerData() {
        return Map.copyOf(this.accelerometerData);
    }

    /**
     * Gets camera data.
     *
     * @return value read by the camera
     */
    public double getCameraData() {
        return this.cameraData;
    }
}
