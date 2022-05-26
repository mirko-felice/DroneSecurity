/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone;

import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Test for Drone Service.
 */
class DroneTest {

    private static final int SENSOR_DATA_READING_WAITING_TIME = 1;
    private static final int TERMINATION_WAITING_TIME = SENSOR_DATA_READING_WAITING_TIME + 3;

    /**
     * Tests drone lifecycle.
     */
    @Test
    void testDroneLifecycle() throws InterruptedException {
        final Drone drone = new Drone();

        drone.activate();
        Assertions.assertTrue(drone.isOperating());

        Assertions.assertEquals(0.0, drone.getProximitySensorData());
        Assertions.assertTrue(drone.getAccelerometerSensorData().isEmpty());
        Assertions.assertEquals(0.0, drone.getCameraSensorData());

        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> {
            drone.readAllData();
            Assertions.assertTrue(drone.getProximitySensorData() > 0.0);

            final Map<String, Double> accelerometerValues = drone.getAccelerometerSensorData();
            Assertions.assertTrue(
                    accelerometerValues.containsKey(MqttMessageParameterConstants.ROLL));
            Assertions.assertTrue(
                    accelerometerValues.containsKey(MqttMessageParameterConstants.PITCH));
            Assertions.assertTrue(
                    accelerometerValues.containsKey(MqttMessageParameterConstants.YAW));

            Assertions.assertTrue(drone.getCameraSensorData() > 0.0);

            drone.deactivate();
            Assertions.assertFalse(drone.isOperating());
        }, SENSOR_DATA_READING_WAITING_TIME, TimeUnit.SECONDS);
        executor.shutdown();
        Assertions.assertTrue(executor.awaitTermination(TERMINATION_WAITING_TIME, TimeUnit.SECONDS));
    }
}
