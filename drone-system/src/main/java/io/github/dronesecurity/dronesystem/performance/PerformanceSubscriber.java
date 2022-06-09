/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttTopicConstants;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.crt.mqtt.MqttMessage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class representing the user side, thus subscribing to the topic that shares drone sensor data.
 */
public class PerformanceSubscriber {

    private final PrintWriter cameraWriter;
    private final PrintWriter accelerometerWriter;

    /**
     *
     * @param cameraOutputFile The file on which write tracked camera performance details
     * @param accelerometerOutputFile The file on which write tracked accelerometer performance details
     * @throws IOException If the specified file does not exist
     */
    public PerformanceSubscriber(final File cameraOutputFile, final File accelerometerOutputFile) throws IOException {
        this.cameraWriter = new PrintWriter(cameraOutputFile, StandardCharsets.UTF_8);
        this.accelerometerWriter = new PrintWriter(accelerometerOutputFile, StandardCharsets.UTF_8);
    }

    /**
     * Subscribes to the topic that shares data read by the camera installed on the drone.
     */
    public void subscribeToCameraPerformance() {
        OutputHelper.printIntro(this.cameraWriter, "Camera performance (Subscriber)");
        OutputHelper.printIntro(this.accelerometerWriter, "AccelerometerPerformance (Subscriber)");

        Connection.getInstance().subscribe(MqttTopicConstants.PERFORMANCE_TOPIC, this::cameraPerformanceHandler);
    }

    /**
     * Terminates the subscriber closing the file writer.
     */
    public void stop() {
        this.cameraWriter.flush();
        this.accelerometerWriter.flush();

        this.cameraWriter.close();
        this.accelerometerWriter.flush();
    }

    private void cameraPerformanceHandler(final @NotNull MqttMessage message) {
        try {
            final JsonNode messageJson = new ObjectMapper()
                    .readTree(new String(message.getPayload(), StandardCharsets.UTF_8));

            final JsonNode cameraJson = messageJson.get(MqttMessageParameterConstants.CAMERA_PARAMETER);
            final int imageSize = cameraJson.get(MqttMessageParameterConstants.IMAGE_SIZE).asInt();
            if (imageSize > 0) {
                final long cameraDataTimestamp = cameraJson.get(MqttMessageParameterConstants.TIMESTAMP).asLong();
                OutputHelper.printCameraPerformanceBySubscriber(this.cameraWriter, imageSize, cameraDataTimestamp);
            }

            final JsonNode accelerometerJson = messageJson.get(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER);
            final Map<String, Double> accelerometerData = new ConcurrentHashMap<>();
            accelerometerData.put(MqttMessageParameterConstants.PITCH,
                    accelerometerJson.get(MqttMessageParameterConstants.PITCH).asDouble());
            accelerometerData.put(MqttMessageParameterConstants.ROLL,
                    accelerometerJson.get(MqttMessageParameterConstants.ROLL).asDouble());
            accelerometerData.put(MqttMessageParameterConstants.YAW,
                    accelerometerJson.get(MqttMessageParameterConstants.YAW).asDouble());
            final long accelerometerDataTimestamp =
                    accelerometerJson.get(MqttMessageParameterConstants.TIMESTAMP).asLong();

            OutputHelper.printAccelerometerPerformanceBySubscriber(this.accelerometerWriter,
                    accelerometerData,
                    accelerometerDataTimestamp);
        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error("Invalid message received.", e);
        }
    }
}
