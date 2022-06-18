/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.monitoring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.lib.AlertLevel;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttTopicConstants;
import io.github.dronesecurity.userapplication.monitoring.entities.MonitoringDroneData;
import io.github.dronesecurity.userapplication.monitoring.entities.MonitoringDroneDataImpl;
import io.github.dronesecurity.userapplication.monitoring.repo.DataRepository;
import io.github.dronesecurity.userapplication.events.*;
import io.github.dronesecurity.userapplication.shipping.entities.Order;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service that monitors drone data channels.
 */
public final class UserMonitoringService {

    private static final String JSON_ERROR_MESSAGE = "Can NOT read json correctly.";
    private final long orderId;

    /**
     * Build the service, automatically subscribed to alert topic.
     * @param orderId {@link Order} identifier to monitor
     */
    public UserMonitoringService(final long orderId) {
        this.orderId = orderId;
    }

    /**
     * Subscribes to the reading topic.
     */
    public void subscribeToDataRead() {
        Connection.getInstance().subscribe(MqttTopicConstants.DATA_TOPIC + this.orderId, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload(), StandardCharsets.UTF_8));
            final double proximity = json.getDouble(MqttMessageParameterConstants.PROXIMITY_PARAMETER);

            final JsonObject accelerometerJson =
                    json.getJsonObject(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER);
            final Map<String, Integer> accelerometer = new ConcurrentHashMap<>();
            if (!accelerometerJson.isEmpty()) {
                accelerometer.put(MqttMessageParameterConstants.ROLL,
                        accelerometerJson.getInteger(MqttMessageParameterConstants.ROLL));
                accelerometer.put(MqttMessageParameterConstants.PITCH,
                        accelerometerJson.getInteger(MqttMessageParameterConstants.PITCH));
                accelerometer.put(MqttMessageParameterConstants.YAW,
                        accelerometerJson.getInteger(MqttMessageParameterConstants.YAW));
            }

            final long camera = json.getLong(MqttMessageParameterConstants.CAMERA_PARAMETER);

            DomainEvents.raise(new DataRead(proximity, accelerometer, camera));

            DataRepository.getInstance().save(
                    new MonitoringDroneDataImpl(proximity, accelerometer, camera, Instant.now(), this.orderId));
        });
    }

    /**
     * Subscribes to drone status topic.
     */
    public void subscribeToOrderStatusChange() {
        Connection.getInstance().subscribe(MqttTopicConstants.LIFECYCLE_TOPIC + this.orderId, msg -> {
            try {
                final JsonNode json = new ObjectMapper().readTree(new String(msg.getPayload(), StandardCharsets.UTF_8));
                if (json.has(MqttMessageParameterConstants.STATUS_PARAMETER)) {
                    final String status = json.get(MqttMessageParameterConstants.STATUS_PARAMETER).asText();
                    DomainEvents.raise(new StatusChanged(status));
                } else if (json.has(MqttMessageParameterConstants.DRONE_MOVING_STATE_PARAMETER)) {
                    final String movingState = json
                            .get(MqttMessageParameterConstants.DRONE_MOVING_STATE_PARAMETER).asText();
                    DomainEvents.raise(new DroneMovingStateChangeEvent(movingState));
                }
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(getClass()).error(JSON_ERROR_MESSAGE, e);
            }
        });
    }

    /**
     * Subscribes to drone alerts' topic.
     */
    public void subscribeToAlerts() {
        Connection.getInstance().subscribe(MqttTopicConstants.ALERT_LEVEL_TOPIC + this.orderId, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload(), StandardCharsets.UTF_8));
            final String alertLevel = json.getString(MqttMessageParameterConstants.ALERT_LEVEL_PARAMETER);
            final String alertType = json.getString(MqttMessageParameterConstants.ALERT_TYPE_PARAMETER);
            switch (AlertLevel.valueOf(alertLevel)) {
                case CRITICAL:
                    DomainEvents.raise(new CriticalSituation(alertType));
                    break;
                case WARNING:
                    DomainEvents.raise(new DangerousSituation(alertType));
                    break;
                case STABLE:
                    DomainEvents.raise(new StableSituation());
                    break;
                default:
            }
        });
    }

    /**
     * Retrieves data history related to the order monitoring.
     * @return the {@link Future} containing the {@link List} of {@link MonitoringDroneData}
     */
    public Future<List<MonitoringDroneData>> retrieveDataHistory() {
        return DataRepository.getInstance().retrieveDataHistory(this.orderId);
    }
}
