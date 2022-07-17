/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.drone.entities;

import io.github.dronesecurity.dronesystem.performance.domain.averages.objects.AveragePerformanceData;
import io.github.dronesecurity.dronesystem.performance.domain.sensor.entities.PerformanceSensorSet;

import java.io.IOException;

/**
 * Class representing a Drone specifically build to evaluate its performance.
 */
public class PerformanceDrone {

    private final PerformanceSensorSet sensorSet;

    /**
     * Builds the drone.
     * @throws IOException whether a sensor of the set failed to create their writer that keeps the
     *                     track of the performances
     */
    public PerformanceDrone() throws IOException {
        this.sensorSet = new PerformanceSensorSet();
    }

    /**
     * Activates the sensor set.
     */
    public void activate() {
        this.sensorSet.activate();
    }

    /**
     * Deactivates the sensor set.
     */
    public void deactivate() {
        this.sensorSet.deactivate();
    }

    /**
     * Performs the reading of the sensor set.
     */
    public void performReading() {
        this.sensorSet.performReading();
    }

    /**
     * Publishes performance sensor set data.
     */
    public void publishData() {
        this.sensorSet.publishData();
    }

    /**
     * Gets the average values of the sensor set.
     * @return average performance of each sensor
     */
    public AveragePerformanceData getAveragePerformances() {
        return this.sensorSet.getAveragePerformances();
    }
}
