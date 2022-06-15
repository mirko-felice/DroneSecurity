/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test for Sensors.
 */
class SensorTest {

    private static final SensorFactory SENSOR_FACTORY = new SensorFactory();

    /**
     * Tests the instantiation of 3 available types of sensor.
     *
     * @param sensorType the type of the sensor to test
     */
    @ParameterizedTest
    @ValueSource(classes = {ProximitySensor.class, Accelerometer.class, Camera.class})
    void sensorCreationTest(final Class<Sensor<?>> sensorType) {
        final Sensor<?> sensor = this.initSensor(sensorType);
        Assertions.assertInstanceOf(sensorType, sensor);
        sensor.activate();
        Assertions.assertTrue(sensor.isOn());
    }

    /**
     * Tests the deactivation of 3 available types of sensor.
     *
     * @param sensorType the type of the sensor to test
     */
    @ParameterizedTest
    @ValueSource(classes = {ProximitySensor.class, Accelerometer.class, Camera.class})
    void sensorDeactivationTest(final Class<Sensor<?>> sensorType) {
        final Sensor<?> sensor = this.initSensor(sensorType);
        sensor.activate();
        Assertions.assertTrue(sensor.isOn());
        sensor.deactivate();
        Assertions.assertFalse(sensor.isOn());
    }

    /**
     * Instantiates a desired type of sensor.
     *
     * @param sensorClass type of the sensor to instantiate
     * @return the sensor instantiated
     */
    private Sensor<?> initSensor(final @NotNull Class<Sensor<?>> sensorClass) {
        if (sensorClass.equals(ProximitySensor.class))
            return SENSOR_FACTORY.getProximitySensor();
        else if (sensorClass.equals(Accelerometer.class))
            return SENSOR_FACTORY.getAccelerometer();
        else
            return SENSOR_FACTORY.getCamera();
    }
}
