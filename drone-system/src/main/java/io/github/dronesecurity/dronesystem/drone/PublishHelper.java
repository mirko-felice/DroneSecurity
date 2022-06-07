/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.lib.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Utility class to publish data and other information using {@link Connection}.
 */
public final class PublishHelper {

    private PublishHelper() { }

    /**
     * Publish all the data provided.
     * @param orderId order identifier needed in order to publish correctly
     * @param proximitySensorData proximity data
     * @param accelerometerSensorData accelerometer data
     * @param cameraSensorData camera data
     */
    public static void publishData(final String orderId,
                                   final Double proximitySensorData,
                                   final @NotNull Map<String, Double> accelerometerSensorData,
                                   final Byte @NotNull [] cameraSensorData) {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode mapJson = mapper.createObjectNode();
        mapJson.put(MqttMessageParameterConstants.PROXIMITY_PARAMETER, proximitySensorData);
        final ObjectNode accelerometerValues = mapper.createObjectNode();
        accelerometerSensorData.forEach(accelerometerValues::put);
        mapJson.set(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER, accelerometerValues);
        mapJson.put(MqttMessageParameterConstants.CAMERA_PARAMETER, cameraSensorData.length);

        Connection.getInstance().publish(MqttTopicConstants.DATA_TOPIC + orderId, mapJson);
    }

    /**
     * Publish the current {@link AlertLevel} on the related topic.
     * @param orderId order identifier needed in order to publish correctly
     * @param currentAlertLevel current alert level to publish
     * @param type {@link AlertType} that causes the alert
     */
    public static void publishCurrentAlertLevel(final String orderId,
                                                final @NotNull AlertLevel currentAlertLevel,
                                                final @NotNull AlertType type) {
        final ObjectNode payload = new ObjectMapper().createObjectNode();
        payload.put(MqttMessageParameterConstants.ALERT_LEVEL_PARAMETER, String.valueOf(currentAlertLevel));
        payload.put(MqttMessageParameterConstants.ALERT_TYPE_PARAMETER, type.toString());
        Connection.getInstance().publish(MqttTopicConstants.ALERT_LEVEL_TOPIC + orderId, payload);
    }

    /**
     * Publish current drone status.
     * @param orderId order identifier needed in order to publish correctly
     * @param status status to publish
     */
    public static void publishCurrentStatus(final String orderId, final String status) {
        final ObjectNode payload = new ObjectMapper().createObjectNode();
        payload.put(MqttMessageParameterConstants.STATUS_PARAMETER, status);
        Connection.getInstance().publish(MqttTopicConstants.LIFECYCLE_TOPIC + orderId, payload);
    }
}
