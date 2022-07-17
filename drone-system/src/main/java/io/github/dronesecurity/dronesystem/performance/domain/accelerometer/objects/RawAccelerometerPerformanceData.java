/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.accelerometer.objects;

import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.RawAccelerometerData;
import io.github.dronesecurity.dronesystem.performance.domain.drone.objects.PerformanceData;

/**
 * POJO class for raw accelerometer performance data.
 */
public class RawAccelerometerPerformanceData extends PerformanceData {

    private final RawAccelerometerData accelerometerData;

    /**
     * Builds accelerometer performance data structure.
     * @param index The index of this data reading related to the data flow
     * @param timestamp The timestamp of when the data were read, represented as the difference,
     *                  measured in milliseconds, between the current time and midnight, January 1, 1970 UTC
     * @param accelerometerData The data read from the accelerometer sensor
     */
    public RawAccelerometerPerformanceData(final int index,
                                           final long timestamp,
                                           final RawAccelerometerData accelerometerData) {
        super(index, timestamp);
        this.accelerometerData = accelerometerData;
    }

    /**
     * Gets the data read by the accelerometer.
     * @return The performance data of the reading
     */
    public RawAccelerometerData getAccelerometerData() {
        return this.accelerometerData;
    }
}
