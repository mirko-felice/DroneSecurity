/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.accelerometer.services;

import io.github.dronesecurity.dronesystem.performance.domain.accelerometer.entities.PerformanceAccelerometer;
import io.github.dronesecurity.dronesystem.performance.domain.accelerometer.objects.ProcessedAccelerometerPerformanceData;

/**
 * Publisher of the {@link ProcessedAccelerometerPerformanceData}.
 */
public interface AccelerometerPerformancePublisher {

    /**
     * Builds and publishes data read by the accelerometer to its topic.
     * @param accelerometerPerformanceData data of the {@link PerformanceAccelerometer} to be sent
     */
    void publishAccelerometer(ProcessedAccelerometerPerformanceData accelerometerPerformanceData);
}
