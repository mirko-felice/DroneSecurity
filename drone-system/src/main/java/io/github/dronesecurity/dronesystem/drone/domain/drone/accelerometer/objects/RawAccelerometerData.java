/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects;

import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.utilities.AccelerometerConstants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class representing accelerometer data as they were read by the sensor.
 */
public class RawAccelerometerData {

    private final double x;
    private final double y;
    private final double z;

    /**
     * Builds the raw accelerometer data.
     *
     * @param x the x value read by the sensor
     * @param y the y value read by the sensor
     * @param z the z value read by the sensor
     */
    public RawAccelerometerData(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Gets X value of the accelerometer.
     * @return the acceleration on the X axis of the accelerometer
     */
    public double getX() {
        return this.x;
    }

    /**
     * Gets Y value of the accelerometer.
     * @return the acceleration on the Y axis of the accelerometer
     */
    public double getY() {
        return this.y;
    }

    /**
     * Gets Z value of the accelerometer.
     * @return the acceleration on the Z axis of the accelerometer
     */
    public double getZ() {
        return this.z;
    }

    /**
     * Gets the accelerometer raw data as a map.
     * @return the map with respective X/Y/Z values
     */
    public Map<String, Double> asMap() {
        final Map<String, Double> values = new ConcurrentHashMap<>();
        values.put(AccelerometerConstants.X, this.x);
        values.put(AccelerometerConstants.Y, this.y);
        values.put(AccelerometerConstants.Z, this.z);
        return values;
    }
}
