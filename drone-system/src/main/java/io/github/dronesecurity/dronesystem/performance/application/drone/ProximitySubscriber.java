/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.application.drone;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.ProcessedProximityData;
import io.github.dronesecurity.dronesystem.performance.domain.proximity.objects.ProximityPerformanceData;
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
 * Class representing the user side, thus subscribing to the topic that shares proximity sensor data.
 */
public class ProximitySubscriber {

    private static final String SPACE_SEPARATOR = " ";
    private static final String INVALID_MESSAGE_EXCEPTION_MSG = "Invalid message received.";

    private final PrintWriter proximityWriter;

    private long totalProximitySubscriberDelay;
    private long totalProximitySubscriberReadings;

    /**
     * Instantiates the performance subscriber with its file writer.
     *
     * @param proximityOutputFile The file on which write tracked proximity sensor performance details
     * @throws IOException If the specified file does not exist
     */
    public ProximitySubscriber(final File proximityOutputFile) throws IOException {
        this.proximityWriter = new PrintWriter(proximityOutputFile, StandardCharsets.UTF_8);
    }

    /**
     * Subscribes to the topic that shares data read by the camera installed on the drone.
     */
    public void subscribeToProximityPerformance() {
        PerformanceOutputHelper.printIntro(this.proximityWriter, "Proximity sensor Performance (Subscriber)");

        final Connection connection = Connection.getInstance();

        connection.subscribe(MqttTopicConstants.PERFORMANCE_TOPIC + SPACE_SEPARATOR
                + MqttMessageParameterConstants.PROXIMITY_PARAMETER, this::proximityPerformanceHandler);
    }

    /**
     * Terminates the subscriber closing the file writer.
     */
    public void stop() {
        this.proximityWriter.flush();
        this.proximityWriter.close();
    }

    /**
     * Gets the average delay of the proximity subscriber.
     * @return The average delay
     */
    public long getAverageDelay() {
        return this.totalProximitySubscriberDelay / this.totalProximitySubscriberReadings;
    }

    private void proximityPerformanceHandler(final @NotNull MqttMessage message) {
        try {
            final JsonNode proximityJson = new ObjectMapper()
                    .readTree(new String(message.getPayload(), StandardCharsets.UTF_8));
            final ProcessedProximityData distance =
                    new ProcessedProximityData(proximityJson.get(PerformanceStringConstants.DISTANCE_PARAMETER)
                            .asDouble());
            final long proximitySensorTimestamp = proximityJson.get(PerformanceStringConstants.TIMESTAMP).asLong();
            final int proximitySensorIndex = proximityJson.get(PerformanceStringConstants.INDEX).asInt();

            final long proximityDelay = System.currentTimeMillis() - proximitySensorTimestamp;
            this.totalProximitySubscriberDelay += proximityDelay;
            this.totalProximitySubscriberReadings++;
            PerformanceOutputHelper.printProximityPerformance(this.proximityWriter,
                    new ProximityPerformanceData(proximitySensorIndex, proximitySensorTimestamp, distance),
                    proximityDelay);

        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error(INVALID_MESSAGE_EXCEPTION_MSG, e);
        }
    }
}
