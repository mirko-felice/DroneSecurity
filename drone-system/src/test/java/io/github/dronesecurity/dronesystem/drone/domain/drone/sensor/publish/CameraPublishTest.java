/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.publish;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.drone.application.drone.camera.CameraDataPublisherImpl;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.ProcessedCameraData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.services.CameraDataPublisher;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
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
 * Class containing tests for publishing of the camera data.
 */
class CameraPublishTest {

    private static final String THREAD_INTERRUPTED_EXCEPTION_MSG = "Thread interrupted.";
    private static final String SPACE_SEPARATOR = " ";

    private static final int IMAGE_TEST_SIZE = 100;

    private static final int ORDER_ID = 1;
    private static final String COURIER = "courier";

    private static final int SUBSCRIPTION_WAITING_TIME = 1;

    private final OrderData orderData;

    private final CountDownLatch latch;
    private final ScheduledExecutorService executorService;

    private JsonNode cameraJsonReceived;

    /**
     * Build order data used by the tests.
     */
    /* default */ CameraPublishTest() {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.latch = new CountDownLatch(1);
        this.orderData = new OrderData(ORDER_ID, COURIER);
    }

    /**
     * Opens the connection.
     */
    @BeforeAll
    /* default */ static void openConnection() {
        Connection.getInstance().connect();
    }

    /**
     * Closes the connection.
     */
    @AfterAll
    /* default */ static void closeConnection() {
        Connection.getInstance().closeConnection();
    }

    /**
     * Tests the publishing of the camera data.
     */
    @Test
    void cameraPublisherTest() {
        final ProcessedCameraData cameraData = new ProcessedCameraData(IMAGE_TEST_SIZE);
        final CameraDataPublisher publisher = new CameraDataPublisherImpl();

        Connection.getInstance().subscribe(MqttTopicConstants.DATA_TOPIC + this.orderData.getOrderId()
                + SPACE_SEPARATOR + MqttMessageParameterConstants.CAMERA_PARAMETER, this::onCameraDataReceived);

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
            LoggerFactory.getLogger(getClass()).error(THREAD_INTERRUPTED_EXCEPTION_MSG, e);
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
