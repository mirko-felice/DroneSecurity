/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.application.drone;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.ProcessedCameraData;
import io.github.dronesecurity.dronesystem.performance.domain.camera.objects.CameraPerformanceData;
import io.github.dronesecurity.dronesystem.performance.utilities.PerformanceStringConstants;
import io.github.dronesecurity.lib.connection.Connection;
import io.github.dronesecurity.lib.connection.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.connection.MqttTopicConstants;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.crt.mqtt.MqttMessage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Class representing the user side, thus subscribing to the topic that shares camera data.
 */
public class CameraSubscriber {

    private static final String SPACE_SEPARATOR = " ";
    private static final String INVALID_MESSAGE_EXCEPTION_MSG = "Invalid message received.";

    private final PrintWriter cameraWriter;

    private long totalCameraSubscriberDelay;
    private long totalCameraSubscriberReadings;

    /**
     * Instantiates the performance subscriber with its file writers.
     *
     * @param cameraOutputFile The file on which write tracked camera performance details
     * @throws IOException If the specified file does not exist
     */
    public CameraSubscriber(final File cameraOutputFile) throws IOException {
        this.cameraWriter = new PrintWriter(cameraOutputFile, StandardCharsets.UTF_8);
    }

    /**
     * Subscribes to the topic that shares data read by the camera installed on the drone.
     */
    public void subscribeToCameraPerformance() {
        PerformanceOutputHelper.printIntro(this.cameraWriter, "Camera performance (Subscriber)");

        final Connection connection = Connection.getInstance();

        connection.subscribe(MqttTopicConstants.PERFORMANCE_TOPIC + SPACE_SEPARATOR
                + MqttMessageParameterConstants.CAMERA_PARAMETER, this::cameraPerformanceHandler);
    }

    /**
     * Terminates the subscriber closing the file writer.
     */
    public void stop() {
        this.cameraWriter.flush();
        this.cameraWriter.close();
    }

    /**
     * Gets the average delay of the camera subscriber.
     * @return The average delay
     */
    public long getAverageDelay() {
        return this.totalCameraSubscriberDelay / this.totalCameraSubscriberReadings;
    }

    private void cameraPerformanceHandler(final @NotNull MqttMessage message) {
        try {
            final JsonNode cameraJson = new ObjectMapper()
                    .readTree(new String(message.getPayload(), StandardCharsets.UTF_8));
            final ProcessedCameraData cameraData =
                    new ProcessedCameraData(cameraJson.get(PerformanceStringConstants.IMAGE_SIZE).asInt());
            if (cameraData.getImageLength() > 0) {
                final long cameraDataTimestamp = cameraJson.get(PerformanceStringConstants.TIMESTAMP).asLong();
                final int cameraDataIndex = cameraJson.get(PerformanceStringConstants.INDEX).asInt();

                final long cameraDelay = System.currentTimeMillis() - cameraDataTimestamp;
                this.totalCameraSubscriberDelay += cameraDelay;
                this.totalCameraSubscriberReadings++;
                PerformanceOutputHelper.printCameraPerformance(this.cameraWriter,
                        new CameraPerformanceData(cameraDataIndex, cameraDataTimestamp, cameraData),
                        cameraDelay);
            }

        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error(INVALID_MESSAGE_EXCEPTION_MSG, e);
        }
    }
}
