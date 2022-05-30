/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone;

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
        final Sensor<Map<String, Double>> accelerometer = new Accelerometer();
        accelerometer.activate();
        return accelerometer;
    }

    /**
     * Creates a proximity sensor.
     *
     * @return The instantiated proximity sensor
     */
    public Sensor<Double> getProximitySensor() {
        final Sensor<Double> proximitySensor = new ProximitySensor();
        proximitySensor.activate();
        return proximitySensor;
    }

    /**
     * Creates a camera.
     *
     * @return The instantiated camera
     */
    public Sensor<Byte[]> getCamera() {
        final Sensor<Byte[]> camera = new Camera();
        camera.activate();
        return camera;
    }
}
