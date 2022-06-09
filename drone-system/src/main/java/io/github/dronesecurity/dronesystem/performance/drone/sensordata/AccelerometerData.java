/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.drone.sensordata;

import java.util.Map;

/**
 * POJO class for accelerometer performance data.
 */
public class AccelerometerData extends PerformanceData {

    private final Map<String, Double> data;

    /**
     * Builds accelerometer performance data structure.
     * @param index The index of this data reading related to the data flow
     * @param timestamp The timestamp of when the data were read, represented as the difference,
     *                  measured in milliseconds, between the current time and midnight, January 1, 1970 UTC
     * @param accelerometerData The data read from the accelerometer sensor
     */
    public AccelerometerData(final int index, final long timestamp, final Map<String, Double> accelerometerData) {
        super(index, timestamp);
        this.data = Map.copyOf(accelerometerData);
    }

    /**
     * Gets the data read by the accelerometer.
     * @return The map with accelerometer sensor data
     */
    public Map<String, Double> getData() {
        return Map.copyOf(this.data);
    }
}
