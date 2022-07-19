/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.drone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.drone.domain.drone.drone.entities.Drone;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.exceptions.SensorNotActivatedException;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttTopicConstants;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.crt.mqtt.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

/**
 * Class containing tests for publishing of the drone data.
 */
class DronePublishingTest {

    private static final int ORDER_ID = 1;
    private static final String COURIER = "courier";
    private static final String DRONE_ID = "Test Drone";

    private static final int PUBLISH_AMOUNT = 3;
    private static final int SUBSCRIPTION_WAITING_TIME = 1;

    private final CountDownLatch latch;
    private final ScheduledExecutorService executorService;

    private JsonNode proximityJson;
    private JsonNode accelerometerJson;
    private JsonNode cameraJson;

    /**
     * Builds the latch for publishing test.
     */
    DronePublishingTest() {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.latch = new CountDownLatch(PUBLISH_AMOUNT);
    }

    /**
     * Opens the connection.
     */
    @BeforeAll
    static void openConnection() {
        Connection.getInstance().connect();
    }

    /**
     * Closes the connection.
     */
    @AfterAll
    static void closeConnection() {
        Connection.getInstance().closeConnection();
    }

    /**
     * Tests publishing of the sensor data.
     */
    @Test
    void publishSensorTest() {
        final Drone drone = new Drone(DRONE_ID);
        final OrderData orderData = new OrderData(ORDER_ID, COURIER);

        Assertions.assertThrows(SensorNotActivatedException.class, drone::publishSensorData,
                "The drone should throw if it wasn't activated first.");

        drone.activate(orderData);

        final Connection connection = Connection.getInstance();

        connection.subscribe(MqttTopicConstants.DATA_TOPIC + orderData.getOrderId()
                + " " + MqttMessageParameterConstants.PROXIMITY_PARAMETER, this::onProximityData);
        connection.subscribe(MqttTopicConstants.DATA_TOPIC + orderData.getOrderId()
                + " " + MqttMessageParameterConstants.ACCELEROMETER_PARAMETER, this::onAccelerometerData);
        connection.subscribe(MqttTopicConstants.DATA_TOPIC + orderData.getOrderId()
                + " " + MqttMessageParameterConstants.CAMERA_PARAMETER, this::onCameraData);

        try {
            this.executorService.schedule(() -> {
                drone.performReading();
                drone.publishSensorData();
                this.executorService.shutdown();
            }, SUBSCRIPTION_WAITING_TIME, TimeUnit.SECONDS);

            this.latch.await();
            Assertions.assertTrue(this.proximityJson.has(MqttMessageParameterConstants.PROXIMITY_PARAMETER));
            Assertions.assertTrue(this.accelerometerJson.has(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER));
            Assertions.assertTrue(this.cameraJson.has(MqttMessageParameterConstants.CAMERA_PARAMETER));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LoggerFactory.getLogger(getClass()).error("Thread interrupted.", e);
        }
    }

    private void onProximityData(final @NotNull MqttMessage message) {
        try {
            this.proximityJson = new ObjectMapper().readTree(new String(message.getPayload(), StandardCharsets.UTF_8));
            this.latch.countDown();
        } catch (JsonProcessingException e) {
            LoggerFactory.getLogger(getClass()).error("Could not read proximity data message received.", e);
        }
    }

    private void onAccelerometerData(final @NotNull MqttMessage message) {
        try {
            this.accelerometerJson =
                    new ObjectMapper().readTree(new String(message.getPayload(), StandardCharsets.UTF_8));
            this.latch.countDown();
        } catch (JsonProcessingException e) {
            LoggerFactory.getLogger(getClass()).error("Could not read accelerometer data message received.", e);
        }
    }

    private void onCameraData(final @NotNull MqttMessage message) {
        try {
            this.cameraJson = new ObjectMapper().readTree(new String(message.getPayload(), StandardCharsets.UTF_8));
            this.latch.countDown();
        } catch (JsonProcessingException e) {
            LoggerFactory.getLogger(getClass()).error("Could not read camera data message received.", e);
        }
    }
}
