/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Test for Drone Service.
 */
class DroneTest {

    private static final int SENSOR_DATA_READING_WAITING_TIME = 4;
    private static final int CAMERA_READING_WAITING_TIME = 3;

    /**
     * Tests drone lifecycle.
     */
    @Test
    void testDroneLifecycle() throws InterruptedException {
        final Drone drone = new Drone("Test Drone");

        drone.activate();
        Assertions.assertTrue(drone.isOperating());

        Assertions.assertEquals(0.0, drone.getProximitySensorData());
        Assertions.assertTrue(drone.getAccelerometerSensorData().isEmpty());
        Assertions.assertEquals(0, drone.getCameraSensorData().length);

        TimeUnit.SECONDS.sleep(SENSOR_DATA_READING_WAITING_TIME);
        drone.readAllData();

        TimeUnit.SECONDS.sleep(CAMERA_READING_WAITING_TIME);
        drone.readAllData();

        Assertions.assertTrue(drone.getProximitySensorData() > 0.0);

        final Map<String, Double> accelerometerValues = drone.getAccelerometerSensorData();
        Assertions.assertTrue(
                accelerometerValues.containsKey(AccelerometerConstants.X));
        Assertions.assertTrue(
                accelerometerValues.containsKey(AccelerometerConstants.Y));
        Assertions.assertTrue(
                accelerometerValues.containsKey(AccelerometerConstants.Z));

        Assertions.assertTrue(drone.getCameraSensorData().length > 0);

        drone.deactivate();
        Assertions.assertFalse(drone.isOperating());
    }
}
