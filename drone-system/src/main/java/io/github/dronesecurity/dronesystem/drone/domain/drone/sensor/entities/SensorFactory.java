/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.entities;

import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.entities.Accelerometer;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.entities.Camera;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.entities.ProximitySensor;

/**
 * Factory assisting in sensors' creation.
 */
public class SensorFactory {

    /**
     * Creates an accelerometer.
     *
     * @return The instantiated accelerometer
     */
    public Sensor getAccelerometer() {
        return new Accelerometer();
    }

    /**
     * Creates a proximity sensor.
     *
     * @return The instantiated proximity sensor
     */
    public Sensor getProximitySensor() {
        return new ProximitySensor();
    }

    /**
     * Creates a camera.
     *
     * @return The instantiated camera
     */
    public Sensor getCamera() {
        return new Camera();
    }
}
