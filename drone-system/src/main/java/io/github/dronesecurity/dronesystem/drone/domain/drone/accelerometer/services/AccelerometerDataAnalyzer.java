/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.services;

import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.entities.Accelerometer;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.ProcessedAccelerometerData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;

/**
 * Analyzer that analyzes the data of an {@link Accelerometer}.
 */
public interface AccelerometerDataAnalyzer {

    /**
     * Checks if inclination angle is critical or warning giving back an {@link Alert}.
     * @param accelerometerData processed data collected by the {@link Accelerometer}
     * @return an {@link Alert}
     */
    Alert analyzeAccelerometerData(ProcessedAccelerometerData accelerometerData);
}
