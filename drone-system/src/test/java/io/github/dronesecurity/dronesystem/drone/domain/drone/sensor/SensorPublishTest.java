/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.sensor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.drone.application.drone.accelerometer.AccelerometerDataPublisherImpl;
import io.github.dronesecurity.dronesystem.drone.application.drone.camera.CameraDataPublisherImpl;
import io.github.dronesecurity.dronesystem.drone.application.drone.proximity.ProximityDataPublisherImpl;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.ProcessedAccelerometerData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.services.AccelerometerDataPublisher;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.ProcessedCameraData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.services.CameraDataPublisher;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.ProcessedProximityData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.services.ProximityDataPublisher;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttTopicConstants;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.crt.mqtt.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class containing tests for publishing of the sensor data.
 */
class SensorPublishTest {

    private static final double TEST_DISTANCE = 15.0;
    private static final int TEST_ANGLE = 10;
    private static final int EMPTY_ANGLE = 0;
    private static final int IMAGE_TEST_SIZE = 100;

    private static final int ORDER_ID = 1;
    private static final String COURIER = "courier";

    private static final int SUBSCRIPTION_WAITING_TIME = 1;

    private final OrderData orderData;

    private CountDownLatch latch;
    private ScheduledExecutorService executorService;

    private JsonNode proximityJsonReceived;
    private JsonNode accelerometerJsonReceived;
    private JsonNode cameraJsonReceived;

    /**
     * Build order data used by the tests.
     */
    SensorPublishTest() {
        this.orderData = new OrderData(ORDER_ID, COURIER);
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
     * Initiates the latch for the tests.
     */
    @BeforeEach
    void initLatch() {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.latch = new CountDownLatch(1);
    }

    /**
     * Tests the publishing of the proximity sensor data.
     */
    @Test
    void proximityPublisherTest() {
        final ProcessedProximityData proximityData = new ProcessedProximityData(TEST_DISTANCE);
        final ProximityDataPublisher publisher = new ProximityDataPublisherImpl();

        Connection.getInstance().subscribe(MqttTopicConstants.DATA_TOPIC + this.orderData.getOrderId()
                + " " + MqttMessageParameterConstants.PROXIMITY_PARAMETER, this::onProximityDataReceived);

        try {
            this.executorService.schedule(() -> {
                publisher.publishProximityData(this.orderData, proximityData);
                this.executorService.shutdown();
            }, SUBSCRIPTION_WAITING_TIME, TimeUnit.SECONDS);

            this.latch.await();

            Assertions.assertTrue(this.proximityJsonReceived.has(MqttMessageParameterConstants.PROXIMITY_PARAMETER),
                    "Received data should contain proximity value.");
            Assertions.assertEquals(proximityData.getDistance(),
                    this.proximityJsonReceived.get(MqttMessageParameterConstants.PROXIMITY_PARAMETER).asInt(),
                    "The distance received should be equal to the sent one.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LoggerFactory.getLogger(getClass()).error("Thread interrupted.", e);
        }
    }

    /**
     * Tests the publishing of the accelerometer data.
     */
    @Test
    void accelerometerPublisherTest() {
        final ProcessedAccelerometerData accelerometerData =
                new ProcessedAccelerometerData(EMPTY_ANGLE, TEST_ANGLE, TEST_ANGLE);
        final AccelerometerDataPublisher publisher = new AccelerometerDataPublisherImpl();

        Connection.getInstance().subscribe(MqttTopicConstants.DATA_TOPIC + this.orderData.getOrderId()
                + " " + MqttMessageParameterConstants.ACCELEROMETER_PARAMETER, this::onAccelerometerDataReceived);

        try {
            this.executorService.schedule(() -> {
                publisher.publishAccelerometerData(this.orderData, accelerometerData);
                this.executorService.shutdown();
            }, SUBSCRIPTION_WAITING_TIME, TimeUnit.SECONDS);

            this.latch.await();

            Assertions.assertTrue(
                    this.accelerometerJsonReceived.has(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER),
                    "Received data should contain accelerometer values.");

            final JsonNode accelerometerValues =
                    this.accelerometerJsonReceived.get(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER);

            Assertions.assertTrue(accelerometerValues.has(MqttMessageParameterConstants.PITCH),
                    "Accelerometer data should contain pitch angle.");
            Assertions.assertEquals(accelerometerData.getPitch(),
                    accelerometerValues.get(MqttMessageParameterConstants.PITCH).asInt(),
                    "The pitch angle value should be equal to the sent one.");

            Assertions.assertTrue(accelerometerValues.has(MqttMessageParameterConstants.ROLL),
                    "Accelerometer data should contain roll angle.");
            Assertions.assertEquals(accelerometerData.getRoll(),
                    accelerometerValues.get(MqttMessageParameterConstants.ROLL).asInt(),
                    "The roll angle value should be equal to the sent one.");

            Assertions.assertTrue(accelerometerValues.has(MqttMessageParameterConstants.YAW),
                    "Accelerometer data should contain yaw angle.");
            Assertions.assertEquals(accelerometerData.getYaw(),
                    accelerometerValues.get(MqttMessageParameterConstants.YAW).asInt(),
                    "The yaw angle value should be equal to the sent one.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LoggerFactory.getLogger(getClass()).error("Thread interrupted.", e);
        }
    }

    /**
     * Tests the publishing of the camera data.
     */
    @Test
    void cameraPublisherTest() {
        final ProcessedCameraData cameraData = new ProcessedCameraData(IMAGE_TEST_SIZE);
        final CameraDataPublisher publisher = new CameraDataPublisherImpl();

        Connection.getInstance().subscribe(MqttTopicConstants.DATA_TOPIC + this.orderData.getOrderId()
                + " " + MqttMessageParameterConstants.CAMERA_PARAMETER, this::onCameraDataReceived);

        try {
            this.executorService.schedule(() -> {
                publisher.publishCameraData(this.orderData, cameraData);
                this.executorService.shutdown();
            }, SUBSCRIPTION_WAITING_TIME, TimeUnit.SECONDS);

            this.latch.await();

            Assertions.assertTrue(this.cameraJsonReceived.has(MqttMessageParameterConstants.CAMERA_PARAMETER),
                    "Received data should contain camera value.");
            Assertions.assertEquals(cameraData.getImageLength(),
                    this.cameraJsonReceived.get(MqttMessageParameterConstants.CAMERA_PARAMETER).asInt(),
                    "The image size received should be equal to the sent one.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LoggerFactory.getLogger(getClass()).error("Thread interrupted.", e);
        }
    }

    private void onProximityDataReceived(final @NotNull MqttMessage message) {
        try {
            this.proximityJsonReceived =
                    new ObjectMapper().readTree(new String(message.getPayload(), StandardCharsets.UTF_8));
            this.latch.countDown();
        } catch (JsonProcessingException e) {
            LoggerFactory.getLogger(getClass()).error("Could not read proximity data message received.", e);
        }
    }

    private void onAccelerometerDataReceived(final @NotNull MqttMessage message) {
        try {
            this.accelerometerJsonReceived =
                    new ObjectMapper().readTree(new String(message.getPayload(), StandardCharsets.UTF_8));
            this.latch.countDown();
        } catch (JsonProcessingException e) {
            LoggerFactory.getLogger(getClass()).error("Could not read accelerometer data message received.", e);
        }
    }

    private void onCameraDataReceived(final @NotNull MqttMessage message) {
        try {
            this.cameraJsonReceived =
                    new ObjectMapper().readTree(new String(message.getPayload(), StandardCharsets.UTF_8));
            this.latch.countDown();
        } catch (JsonProcessingException e) {
            LoggerFactory.getLogger(getClass()).error("Could not read camera data message received.", e);
        }
    }
}
