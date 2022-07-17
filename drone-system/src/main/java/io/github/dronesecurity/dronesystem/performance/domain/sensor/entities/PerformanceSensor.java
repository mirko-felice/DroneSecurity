/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.sensor.entities;

import io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.entities.Sensor;

/**
 * Interface representing a generic sensor that keeps track of its performance data.
 */
public interface PerformanceSensor extends Sensor {

    /**
     * Publishes most recent processed data.
     */
    void publishPerformanceData();

    /**
     * Gets the average delay of the performance computed by the sensor.
     * @return the average reading delay computed by the sensor
     */
    long getAverageDelay();
}
