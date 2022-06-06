/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.DateHelper;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttTopicConstants;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.crt.mqtt.MqttMessage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * Class representing the user side, thus subscribing to the topic that shares drone sensor data.
 */
public class PerformanceSubscriber {

    private final PrintWriter writer;

    /**
     *
     * @param outputFile The file on which write tracked performance details
     * @throws IOException If the specified file does not exist
     */
    public PerformanceSubscriber(final File outputFile) throws IOException {
        this.writer = new PrintWriter(outputFile, StandardCharsets.UTF_8);
    }

    /**
     * Subscribes to the topic that shares data read by the camera installed on the drone.
     */
    public void subscribeToCameraPerformance() {
        this.writer.println(DateHelper.toString(Instant.now()));
        this.writer.println("---");
        this.writer.println();
        Connection.getInstance().subscribe(MqttTopicConstants.PERFORMANCE_CAMERA, this::cameraPerformanceHandler);
    }

    /**
     * Terminates the subscriber closing the file writer.
     */
    public void stop() {
        this.writer.flush();
        this.writer.close();
    }

    private void cameraPerformanceHandler(final @NotNull MqttMessage message) {
        try {
            final JsonNode messageJson = new ObjectMapper()
                    .readTree(new String(message.getPayload(), StandardCharsets.UTF_8));
            final int imageSize = messageJson.get(MqttMessageParameterConstants.CAMERA_PARAMETER).asInt();
            if (imageSize > 0) {
                final long timestamp = messageJson.get(MqttMessageParameterConstants.TIMESTAMP).asLong();

                this.writer.println("Image size - " + imageSize);
                final long delay = System.currentTimeMillis() - timestamp;
                this.writer.println("Delay - " + delay + " ms");
                this.writer.println();
            }
        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error("Invalid message received.", e);
        }
    }
}
