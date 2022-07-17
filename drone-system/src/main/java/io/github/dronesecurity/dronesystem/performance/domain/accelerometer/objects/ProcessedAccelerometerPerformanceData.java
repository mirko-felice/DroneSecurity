/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.accelerometer.objects;

import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.ProcessedAccelerometerData;
import io.github.dronesecurity.dronesystem.performance.domain.drone.objects.PerformanceData;

/**
 * POJO class for processed accelerometer performance data.
 */
public class ProcessedAccelerometerPerformanceData extends PerformanceData {

    private final ProcessedAccelerometerData accelerometerData;

    /**
     * Builds accelerometer performance data structure.
     * @param index The index of this data reading related to the data flow
     * @param timestamp The timestamp of when the data were read, represented as the difference,
     *                  measured in milliseconds, between the current time and midnight, January 1, 1970 UTC
     * @param accelerometerData The data read from the accelerometer sensor
     */
    public ProcessedAccelerometerPerformanceData(final int index,
                                                 final long timestamp,
                                                 final ProcessedAccelerometerData accelerometerData) {
        super(index, timestamp);
        this.accelerometerData = accelerometerData;
    }

    /**
     * Gets the data read by the accelerometer.
     * @return The performance data of the reading
     */
    public ProcessedAccelerometerData getAccelerometerData() {
        return this.accelerometerData;
    }
}
