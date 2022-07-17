/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.services;

import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.ProcessedAccelerometerData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.RawAccelerometerData;

/**
 * Processor that transforms {@link RawAccelerometerData} into {@link ProcessedAccelerometerData}.
 */
public interface AccelerometerDataProcessor {

    /**
     * Elaborate raw accelerometer data to convert into useful angles.
     * @param accelerometerData raw accelerometer data containing the x, y and z values
     * @return a new processed accelerometer data containing roll, pitch and yaw angles
     */
    ProcessedAccelerometerData processAccelerometerData(RawAccelerometerData accelerometerData);
}
