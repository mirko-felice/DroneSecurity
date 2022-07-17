/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.sensor.entities;

import io.github.dronesecurity.dronesystem.performance.domain.accelerometer.entities.PerformanceAccelerometer;
import io.github.dronesecurity.dronesystem.performance.domain.camera.entities.PerformanceCamera;
import io.github.dronesecurity.dronesystem.performance.domain.proximity.entities.PerformanceProximity;

import java.io.IOException;

/**
 * Factory assisting in performance sensors' creation.
 */
public class PerformanceSensorFactory {

    /**
     * Creates an accelerometer.
     *
     * @return The instantiated accelerometer
     * @throws IOException whether the sensor failed to create their writer that keeps the track of the performances
     */
    public PerformanceSensor getAccelerometer() throws IOException {
        return new PerformanceAccelerometer();
    }

    /**
     * Creates a proximity sensor.
     *
     * @return The instantiated proximity sensor
     * @throws IOException whether the sensor failed to create their writer that keeps the track of the performances
     */
    public PerformanceSensor getProximitySensor() throws IOException {
        return new PerformanceProximity();
    }

    /**
     * Creates a camera.
     *
     * @return The instantiated camera
     * @throws IOException whether the sensor failed to create their writer that keeps the track of the performances
     */
    public PerformanceSensor getCamera() throws IOException {
        return new PerformanceCamera();
    }
}
