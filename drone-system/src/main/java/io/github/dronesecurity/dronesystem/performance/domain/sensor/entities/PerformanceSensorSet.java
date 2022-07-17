/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.sensor.entities;

import io.github.dronesecurity.dronesystem.performance.domain.averages.objects.AveragePerformanceData;

import java.io.IOException;

/**
 * A set of all sensors of the drone that keep the tracking of their performances.
 */
public class PerformanceSensorSet {

    private static final PerformanceSensorFactory SENSOR_FACTORY = new PerformanceSensorFactory();
    private final PerformanceSensor proximity;
    private final PerformanceSensor accelerometer;
    private final PerformanceSensor camera;

    /**
     * Builds the sensor set of the sensors with their own performance.
     * @throws IOException whether sensors failed to create their writer that keeps the track of the performances
     */
    public PerformanceSensorSet() throws IOException {
        this.proximity = SENSOR_FACTORY.getProximitySensor();
        this.accelerometer = SENSOR_FACTORY.getAccelerometer();
        this.camera = SENSOR_FACTORY.getCamera();
    }

    /**
     * Activates all the sensors of the set.
     */
    public void activate() {
        this.proximity.activate();
        this.accelerometer.activate();
        this.camera.activate();
    }

    /**
     * Deactivates all the sensors of the set.
     */
    public void deactivate() {
        this.proximity.deactivate();
        this.accelerometer.deactivate();
        this.camera.deactivate();
    }

    /**
     * Performs a single reading on all the sensors of the set.
     */
    public void performReading() {
        this.proximity.performReading();
        this.accelerometer.performReading();
        this.camera.performReading();
    }

    /**
     * Publishes the data of all the sensors of the set.
     */
    public void publishData() {
        this.proximity.publishPerformanceData();
        this.accelerometer.publishPerformanceData();
        this.camera.publishPerformanceData();
    }

    /**
     * Gets the average performance data from all the sensors of the set.
     * @return Container that combines the performance data of all the sensors of the set
     */
    public AveragePerformanceData getAveragePerformances() {
        return new AveragePerformanceData(
                this.proximity.getAverageDelay(),
                this.accelerometer.getAverageDelay(),
                this.camera.getAverageDelay());
    }
}
