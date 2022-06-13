/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.AccelerometerData;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.CameraData;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.ProximityData;
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
        OutputHelper.printIntro(this.cameraWriter, "Camera performance (Subscriber)");
        OutputHelper.printIntro(this.accelerometerWriter, "Accelerometer Performance (Subscriber)");
        OutputHelper.printIntro(this.proximityWriter, "Proximity sensor Performance (Subscriber)");

        Connection.getInstance().subscribe(MqttTopicConstants.PERFORMANCE_TOPIC, this::dronePerformanceHandler);
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
    public Map<String, Long> getAveragePerformance() {
        final Map<String, Long> averagePerformance = new ConcurrentHashMap<>();

        averagePerformance.put(MqttMessageParameterConstants.CAMERA_PARAMETER,
                this.totalCameraSubscriberDelay / this.totalCameraSubscriberReadings);
        averagePerformance.put(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER,
                this.totalAccelerometerSubscriberDelay / this.totalAccelerometerSubscriberReadings);
        averagePerformance.put(MqttMessageParameterConstants.PROXIMITY_PARAMETER,
                this.totalProximitySubscriberDelay / this.totalProximitySubscriberReadings);

        return averagePerformance;
    }

    private void dronePerformanceHandler(final @NotNull MqttMessage message) {
        try {
            final JsonNode messageJson = new ObjectMapper()
                    .readTree(new String(message.getPayload(), StandardCharsets.UTF_8));

            final JsonNode cameraJson = messageJson.get(MqttMessageParameterConstants.CAMERA_PARAMETER);
            final int imageSize = cameraJson.get(PerformanceStringConstants.IMAGE_SIZE).asInt();
            if (imageSize > 0) {
                final long cameraDataTimestamp = cameraJson.get(PerformanceStringConstants.TIMESTAMP).asLong();
                final int cameraDataIndex = cameraJson.get(PerformanceStringConstants.INDEX).asInt();

                final long cameraDelay = System.currentTimeMillis() - cameraDataTimestamp;
                this.totalCameraSubscriberDelay += cameraDelay;
                this.totalCameraSubscriberReadings++;
                OutputHelper.printCameraPerformance(this.cameraWriter,
                        new CameraData(cameraDataIndex, cameraDataTimestamp, imageSize),
                        cameraDelay);
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
                    accelerometerJson.get(PerformanceStringConstants.TIMESTAMP).asLong();
            final int accelerometerDataIndex =
                    accelerometerJson.get(PerformanceStringConstants.INDEX).asInt();

            final long accelerometerDelay = System.currentTimeMillis() - accelerometerDataTimestamp;
            this.totalAccelerometerSubscriberDelay += accelerometerDelay;
            this.totalAccelerometerSubscriberReadings++;
            OutputHelper.printAccelerometerProcessedDataPerformance(this.accelerometerWriter,
                    new AccelerometerData(accelerometerDataIndex, accelerometerDataTimestamp, accelerometerData),
                    accelerometerDelay,
                    false);

            final JsonNode proximityJson = messageJson.get(MqttMessageParameterConstants.PROXIMITY_PARAMETER);
            final double distance = proximityJson.get(PerformanceStringConstants.DISTANCE_PARAMETER).asDouble();
            final long proximitySensorTimestamp = proximityJson.get(PerformanceStringConstants.TIMESTAMP).asLong();
            final int proximitySensorIndex = proximityJson.get(PerformanceStringConstants.INDEX).asInt();

            final long proximityDelay = System.currentTimeMillis() - proximitySensorTimestamp;
            this.totalProximitySubscriberDelay += proximityDelay;
            this.totalProximitySubscriberReadings++;
            OutputHelper.printProximityPerformance(this.proximityWriter,
                    new ProximityData(proximitySensorIndex, proximitySensorTimestamp, distance),
                    proximityDelay);

        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error("Invalid message received.", e);
        }
    }
}
