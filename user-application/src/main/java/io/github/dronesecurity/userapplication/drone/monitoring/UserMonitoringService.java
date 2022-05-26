/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.drone.monitoring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.lib.AlertLevel;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttTopicConstants;
import io.github.dronesecurity.userapplication.events.*;
import io.vertx.core.json.JsonObject;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Service that monitors drone data channels.
 */
public final class UserMonitoringService {

    /**
     * Subscribes to the reading topic.
     *
     * @param consumer {@link Consumer} to execute when a {@link DataRead} is raised
     */
    public void subscribeToDataRead(final Consumer<DataRead> consumer) {
        DomainEvents.register(DataRead.class, consumer);
        Connection.getInstance().subscribe(MqttTopicConstants.DATA_TOPIC, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload(), StandardCharsets.UTF_8));
            final double proximity = json.getDouble(MqttMessageParameterConstants.PROXIMITY_PARAMETER);

            final JsonObject accelerometerJson =
                    json.getJsonObject(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER);
            final Map<String, Double> accelerometer = new ConcurrentHashMap<>();
            accelerometer.put(MqttMessageParameterConstants.ROLL,
                    accelerometerJson.getDouble(MqttMessageParameterConstants.ROLL));
            accelerometer.put(MqttMessageParameterConstants.PITCH,
                    accelerometerJson.getDouble(MqttMessageParameterConstants.PITCH));
            accelerometer.put(MqttMessageParameterConstants.YAW,
                    accelerometerJson.getDouble(MqttMessageParameterConstants.YAW));

            final double camera = json.getDouble(MqttMessageParameterConstants.CAMERA_PARAMETER);

            DomainEvents.raise(new DataRead(proximity, accelerometer, camera));
        });
    }

    /**
     * Subscribes to the warning situations.
     *
     * @param consumer {@link Consumer} to execute when a {@link WarningSituation} is raised
     */
    public void subscribeToWarningSituation(final Consumer<WarningSituation> consumer) {
        DomainEvents.register(WarningSituation.class, consumer);
        Connection.getInstance().subscribe(MqttTopicConstants.ALERT_LEVEL_TOPIC, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload(), StandardCharsets.UTF_8));
            if (json.getString(MqttMessageParameterConstants.ALERT_LEVEL_PARAMETER)
                    .equals(AlertLevel.WARNING.toString()))
                DomainEvents.raise(
                        new WarningSituation(json.getString(MqttMessageParameterConstants.ALERT_TYPE_PARAMETER)));
        });
    }

    /**
     * Subscribes to the critical situations.
     *
     * @param consumer {@link Consumer} to execute when a {@link CriticalSituation} is raised
     */
    public void subscribeToCriticalSituation(final Consumer<CriticalSituation> consumer) {
        DomainEvents.register(CriticalSituation.class, consumer);
        Connection.getInstance().subscribe(MqttTopicConstants.ALERT_LEVEL_TOPIC, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload(), StandardCharsets.UTF_8));
            if (json.getString(MqttMessageParameterConstants.ALERT_LEVEL_PARAMETER)
                    .equals(AlertLevel.CRITICAL.toString()))
                DomainEvents.raise(
                        new CriticalSituation(json.getString(MqttMessageParameterConstants.ALERT_TYPE_PARAMETER)));
        });
    }

    /**
     * Subscribes to the standard situations.
     *
     * @param consumer {@link Consumer} to execute when a {@link StandardSituation} is raised
     */
    public void subscribeToStandardSituation(final Consumer<StandardSituation> consumer) {
        DomainEvents.register(StandardSituation.class, consumer);
        Connection.getInstance().subscribe(MqttTopicConstants.ALERT_LEVEL_TOPIC, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload(), StandardCharsets.UTF_8));
            if (json.getString(MqttMessageParameterConstants.ALERT_LEVEL_PARAMETER)
                    .equals(AlertLevel.NONE.toString()))
                DomainEvents.raise(new StandardSituation());
        });
    }
    /**
     * Subscribes to drone status topic.
     *
     * @param consumer {@link Consumer} to execute when a {@link StatusChanged} is raised
     */
    public void subscribeToOrderStatusChange(final Consumer<StatusChanged> consumer) {
        DomainEvents.register(StatusChanged.class, consumer);
        Connection.getInstance().subscribe(MqttTopicConstants.LIFECYCLE_TOPIC, msg -> {
            try {
                final JsonNode json = new ObjectMapper().readTree(new String(msg.getPayload(), StandardCharsets.UTF_8));
                final String status = json.get(MqttMessageParameterConstants.STATUS_PARAMETER).asText();
                DomainEvents.raise(new StatusChanged(status));
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(getClass()).error("Can NOT read json correctly.", e);
            }
        });
    }
}
