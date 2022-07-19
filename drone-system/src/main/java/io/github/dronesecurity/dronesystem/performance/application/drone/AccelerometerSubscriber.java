/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.application.drone;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.ProcessedAccelerometerData;
import io.github.dronesecurity.dronesystem.performance.domain.accelerometer.objects.ProcessedAccelerometerPerformanceData;
import io.github.dronesecurity.dronesystem.performance.utilities.PerformanceStringConstants;
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
 * Class representing the user side, thus subscribing to the topic that shares accelerometer data.
 */
public class AccelerometerSubscriber {

    private static final String SPACE_SEPARATOR = " ";
    private static final String INVALID_MESSAGE_EXCEPTION_MSG = "Invalid message received.";

    private final PrintWriter accelerometerWriter;

    private long totalAccelerometerSubscriberDelay;
    private long totalAccelerometerSubscriberReadings;

    /**
     * Instantiates the performance subscriber with its file writers.
     *
     * @param accelerometerOutputFile The file on which write tracked accelerometer performance details
     * @throws IOException If the specified file does not exist
     */
    public AccelerometerSubscriber(final File accelerometerOutputFile) throws IOException {
        this.accelerometerWriter = new PrintWriter(accelerometerOutputFile, StandardCharsets.UTF_8);
    }

    /**
     * Subscribes to the topic that shares data read by the camera installed on the drone.
     */
    public void subscribeToAccelerometerPerformance() {
        PerformanceOutputHelper.printIntro(this.accelerometerWriter, "Accelerometer Performance (Subscriber)");

        final Connection connection = Connection.getInstance();

        connection.subscribe(MqttTopicConstants.PERFORMANCE_TOPIC + SPACE_SEPARATOR
                + MqttMessageParameterConstants.ACCELEROMETER_PARAMETER, this::accelerometerPerformanceHandler);
    }

    /**
     * Terminates the subscriber closing the file writer.
     */
    public void stop() {
        this.accelerometerWriter.flush();
        this.accelerometerWriter.close();
    }

    /**
     * Gets the average delay of the accelerometer subscriber.
     * @return The average delay
     */
    public long getAverageDelay() {
        return this.totalAccelerometerSubscriberDelay / this.totalAccelerometerSubscriberReadings;
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
            LoggerFactory.getLogger(getClass()).error(INVALID_MESSAGE_EXCEPTION_MSG, e);
        }
    }
}
