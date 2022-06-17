/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.entities.sensors;

import java.util.Map;

/**
 * Factory assisting in sensors' creation.
 */
public class SensorFactory {

    /**
     * Creates an accelerometer.
     *
     * @return The instantiated accelerometer
     */
    public Sensor<Map<String, Double>> getAccelerometer() {
        return new Accelerometer();
    }

    /**
     * Creates a proximity sensor.
     *
     * @return The instantiated proximity sensor
     */
    public Sensor<Double> getProximitySensor() {
        return new ProximitySensor();
    }

    /**
     * Creates a camera.
     *
     * @return The instantiated camera
     */
    public Sensor<Byte[]> getCamera() {
        return new Camera();
    }
}
