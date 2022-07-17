/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.application.drone;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.RawCameraData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.ProcessedAccelerometerData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.RawProximityData;
import io.github.dronesecurity.dronesystem.performance.utilities.PerformanceStringConstants;
import io.github.dronesecurity.dronesystem.performance.domain.accelerometer.objects.ProcessedAccelerometerPerformanceData;
import io.github.dronesecurity.dronesystem.performance.domain.averages.objects.AveragePerformanceData;
import io.github.dronesecurity.dronesystem.performance.domain.camera.objects.CameraPerformanceData;
import io.github.dronesecurity.dronesystem.performance.domain.proximity.objects.ProximityPerformanceData;
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

/**
 * Class representing the user side, thus subscribing to the topic that shares drone sensor data.
 */
public class PerformanceSubscriber {

    private final PrintWriter cameraWriter;
    private final PrintWriter accelerometerWriter;
    private final PrintWriter proximityWriter;

    private long totalCameraSubscriberDelay;
    private long totalCameraSubscriberReadings;

    private long totalAccelerometerSubscriberDelay;
    private long totalAccelerometerSubscriberReadings;

    private long totalProximitySubscriberDelay;
    private long totalProximitySubscriberReadings;

    /**
     *
     * @param cameraOutputFile The file on which write tracked camera performance details
     * @param accelerometerOutputFile The file on which write tracked accelerometer performance details
     * @param proximityOutputFile The file on which write tracked proximity sensor performance details
     * @throws IOException If the specified file does not exist
     */
    public PerformanceSubscriber(final File cameraOutputFile,
                                 final File accelerometerOutputFile,
                                 final File proximityOutputFile) throws IOException {
        this.cameraWriter = new PrintWriter(cameraOutputFile, StandardCharsets.UTF_8);
        this.accelerometerWriter = new PrintWriter(accelerometerOutputFile, StandardCharsets.UTF_8);
        this.proximityWriter = new PrintWriter(proximityOutputFile, StandardCharsets.UTF_8);
    }

    /**
     * Subscribes to the topic that shares data read by the camera installed on the drone.
     */
    public void subscribeToDronePerformance() {
        PerformanceOutputHelper.printIntro(this.cameraWriter, "Camera performance (Subscriber)");
        PerformanceOutputHelper.printIntro(this.accelerometerWriter, "Accelerometer Performance (Subscriber)");
        PerformanceOutputHelper.printIntro(this.proximityWriter, "Proximity sensor Performance (Subscriber)");

        final Connection connection = Connection.getInstance();

        connection.subscribe(MqttTopicConstants.PERFORMANCE_TOPIC + " "
                + MqttMessageParameterConstants.PROXIMITY_PARAMETER, this::proximityPerformanceHandler);
        connection.subscribe(MqttTopicConstants.PERFORMANCE_TOPIC + " "
                + MqttMessageParameterConstants.ACCELEROMETER_PARAMETER, this::accelerometerPerformanceHandler);
        connection.subscribe(MqttTopicConstants.PERFORMANCE_TOPIC + " "
                + MqttMessageParameterConstants.CAMERA_PARAMETER, this::cameraPerformanceHandler);
    }

    /**
     * Terminates the subscriber closing the file writer.
     */
    public void stop() {
        this.cameraWriter.flush();
        this.accelerometerWriter.flush();
        this.proximityWriter.flush();

        this.cameraWriter.close();
        this.accelerometerWriter.close();
        this.proximityWriter.close();
    }

    /**
     * Gets the averaged delays of all performance evaluations.
     * @return the map containing the averaged performance delays
     */
    public AveragePerformanceData getAveragePerformance() {
        return new AveragePerformanceData(this.totalProximitySubscriberDelay / this.totalProximitySubscriberReadings,
                this.totalAccelerometerSubscriberDelay / this.totalAccelerometerSubscriberReadings,
                this.totalCameraSubscriberDelay / this.totalCameraSubscriberReadings);
    }

    private void proximityPerformanceHandler(final @NotNull MqttMessage message) {
        try {
            final JsonNode proximityJson = new ObjectMapper()
                    .readTree(new String(message.getPayload(), StandardCharsets.UTF_8));
            final RawProximityData distance =
                    new RawProximityData(proximityJson.get(PerformanceStringConstants.DISTANCE_PARAMETER).asDouble());
            final long proximitySensorTimestamp = proximityJson.get(PerformanceStringConstants.TIMESTAMP).asLong();
            final int proximitySensorIndex = proximityJson.get(PerformanceStringConstants.INDEX).asInt();

            final long proximityDelay = System.currentTimeMillis() - proximitySensorTimestamp;
            this.totalProximitySubscriberDelay += proximityDelay;
            this.totalProximitySubscriberReadings++;
            PerformanceOutputHelper.printProximityPerformance(this.proximityWriter,
                    new ProximityPerformanceData(proximitySensorIndex, proximitySensorTimestamp, distance),
                    proximityDelay);

        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error("Invalid message received.", e);
        }
    }

    private void accelerometerPerformanceHandler(final @NotNull MqttMessage message) {
        try {
            final JsonNode accelerometerJson = new ObjectMapper()
                    .readTree(new String(message.getPayload(), StandardCharsets.UTF_8));
            final ProcessedAccelerometerData accelerometerData = new ProcessedAccelerometerData(
                    accelerometerJson.get(MqttMessageParameterConstants.PITCH).asInt(),
                    accelerometerJson.get(MqttMessageParameterConstants.ROLL).asInt(),
                    accelerometerJson.get(MqttMessageParameterConstants.YAW).asInt());

            final long accelerometerDataTimestamp =
                    accelerometerJson.get(PerformanceStringConstants.TIMESTAMP).asLong();
            final int accelerometerDataIndex =
                    accelerometerJson.get(PerformanceStringConstants.INDEX).asInt();

            final long accelerometerDelay = System.currentTimeMillis() - accelerometerDataTimestamp;
            this.totalAccelerometerSubscriberDelay += accelerometerDelay;
            this.totalAccelerometerSubscriberReadings++;
            PerformanceOutputHelper.printAccelerometerPerformance(this.accelerometerWriter,
                    new ProcessedAccelerometerPerformanceData(
                            accelerometerDataIndex,
                            accelerometerDataTimestamp,
                            accelerometerData),
                    accelerometerDelay);

        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error("Invalid message received.", e);
        }
    }

    private void cameraPerformanceHandler(final @NotNull MqttMessage message) {
        try {
            final JsonNode cameraJson = new ObjectMapper()
                    .readTree(new String(message.getPayload(), StandardCharsets.UTF_8));
            final RawCameraData cameraData =
                    new RawCameraData(cameraJson.get(PerformanceStringConstants.IMAGE_SIZE).asInt());
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
            LoggerFactory.getLogger(getClass()).error("Invalid message received.", e);
        }
    }
}
