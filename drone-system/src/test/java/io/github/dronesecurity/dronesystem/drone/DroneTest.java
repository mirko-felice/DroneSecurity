/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone;

import io.github.dronesecurity.dronesystem.drone.entities.Drone;
import io.github.dronesecurity.dronesystem.drone.utilities.AccelerometerConstants;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Test for Drone Service.
 */
class DroneTest {

    private static final int SENSOR_DATA_READING_WAITING_TIME = 10;

    /**
     * Tests drone lifecycle.
     */
    @Test
    void testDroneLifecycle() {
        final Drone drone = new Drone("Test Drone");

        drone.activate();
        Assertions.assertTrue(drone.isOperating());

        Assertions.assertEquals(0.0, drone.getProximitySensorData());
        Assertions.assertTrue(drone.getAccelerometerSensorData().isEmpty());
        Assertions.assertEquals(0, drone.getCameraSensorData().length);

        this.awaitUntil(() -> {
            drone.readAllData();
            return drone.getProximitySensorData() > 0.0;
        });

        this.awaitUntil(() -> {
            drone.readAllData();
            final Map<String, Double> accelerometerValues = drone.getAccelerometerSensorData();
            return accelerometerValues.containsKey(AccelerometerConstants.X)
                    && accelerometerValues.containsKey(AccelerometerConstants.Y)
                    && accelerometerValues.containsKey(AccelerometerConstants.Z);
        });

        this.awaitUntil(() -> {
            drone.readAllData();
            return drone.getCameraSensorData().length > 0;
        });

        drone.deactivate();
        Assertions.assertFalse(drone.isOperating());
    }

    private void awaitUntil(final Callable<Boolean> booleanCallable) {
        Awaitility.await().atMost(SENSOR_DATA_READING_WAITING_TIME, TimeUnit.SECONDS).until(booleanCallable);
    }
}
