/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.averages.objects;

/**
 * Class representing the data regarding the average performance of all sensors.
 */
public class AveragePerformanceData {

    private final long averageProximityDelay;
    private final long averageAccelerometerDelay;
    private final long averageCameraDelay;

    /**
     * Builds the average data of all performance sensors.
     * @param averageProximityDelay Average delay of the proximity sensor
     * @param averageAccelerometerDelay Average delay of the accelerometer sensor
     * @param averageCameraDelay Average delay of the camera sensor
     */
    public AveragePerformanceData(final long averageProximityDelay,
                                  final long averageAccelerometerDelay,
                                  final long averageCameraDelay) {
        this.averageProximityDelay = averageProximityDelay;
        this.averageAccelerometerDelay = averageAccelerometerDelay;
        this.averageCameraDelay = averageCameraDelay;
    }

    /**
     * Gets the average delay of the proximity sensor.
     * @return average delay of the proximity sensor
     */
    public long getAverageProximityDelay() {
        return this.averageProximityDelay;
    }

    /**
     * Gets the average delay of the accelerometer.
     * @return average delay of the accelerometer
     */
    public long getAverageAccelerometerDelay() {
        return this.averageAccelerometerDelay;
    }

    /**
     * Gets the average delay of the camera.
     * @return average delay of the camera
     */
    public long getAverageCameraDelay() {
        return this.averageCameraDelay;
    }
}
