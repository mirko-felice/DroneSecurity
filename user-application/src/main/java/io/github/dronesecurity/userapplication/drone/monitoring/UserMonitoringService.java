/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.drone.monitoring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.lib.*;
import io.github.dronesecurity.userapplication.drone.monitoring.entities.DroneData;
import io.github.dronesecurity.userapplication.drone.monitoring.entities.DroneDataImpl;
import io.github.dronesecurity.userapplication.drone.monitoring.repo.DataRepository;
import io.github.dronesecurity.userapplication.events.*;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Service that monitors drone data channels.
 */
public final class UserMonitoringService {

    private static final String JSON_ERROR_MESSAGE = "Can NOT read json correctly.";
    private final String orderId;

    /**
     * Build the service.
     * @param orderId {@link io.github.dronesecurity.userapplication.shipping.courier.entities.Order} identifier to
     *                                                                                              monitor
     */
    public UserMonitoringService(final String orderId) {
        this.orderId = orderId;
    }

    /**
     * Subscribes to the reading topic.
     *
     * @param consumer {@link Consumer} to execute when a {@link DataRead} is raised
     */
    public void subscribeToDataRead(final Consumer<DataRead> consumer) {
        DomainEvents.register(DataRead.class, consumer);
        Connection.getInstance().subscribe(MqttTopicConstants.DATA_TOPIC + this.orderId, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload(), StandardCharsets.UTF_8));
            final double proximity = json.getDouble(MqttMessageParameterConstants.PROXIMITY_PARAMETER);

            final JsonObject accelerometerJson =
                    json.getJsonObject(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER);
            final Map<String, Double> accelerometer = new ConcurrentHashMap<>();
            // TODO refactor strings
            if (!accelerometerJson.isEmpty()) {
                accelerometer.put(MqttMessageParameterConstants.ROLL,
                        accelerometerJson.getDouble(MqttMessageParameterConstants.ROLL));
                accelerometer.put(MqttMessageParameterConstants.PITCH,
                        accelerometerJson.getDouble(MqttMessageParameterConstants.PITCH));
                accelerometer.put(MqttMessageParameterConstants.YAW,
                        accelerometerJson.getDouble(MqttMessageParameterConstants.YAW));
            }

            final long camera = json.getLong(MqttMessageParameterConstants.CAMERA_PARAMETER);

            DomainEvents.raise(new DataRead(proximity, accelerometer, camera));

            DataRepository.getInstance().save(
                    new DroneDataImpl(proximity, accelerometer, camera, Instant.now(), this.orderId));
        });
    }

    /**
     * Subscribes to the warning situations.
     *
     * @param consumer {@link Consumer} to execute when a {@link WarningSituation} is raised
     */
    public void subscribeToWarningSituation(final Consumer<WarningSituation> consumer) {
        DomainEvents.register(WarningSituation.class, consumer);
        Connection.getInstance().subscribe(MqttTopicConstants.ALERT_LEVEL_TOPIC + this.orderId, msg -> {
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
        Connection.getInstance().subscribe(MqttTopicConstants.ALERT_LEVEL_TOPIC + this.orderId, msg -> {
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
        Connection.getInstance().subscribe(MqttTopicConstants.ALERT_LEVEL_TOPIC + this.orderId, msg -> {
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
        Connection.getInstance().subscribe(MqttTopicConstants.LIFECYCLE_TOPIC + this.orderId, msg -> {
            try {
                final JsonNode json = new ObjectMapper().readTree(new String(msg.getPayload(), StandardCharsets.UTF_8));
                final String status = json.get(MqttMessageParameterConstants.STATUS_PARAMETER).asText();
                DomainEvents.raise(new StatusChanged(status));
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(getClass()).error(JSON_ERROR_MESSAGE, e);
            }
        });
    }

    // TODO negligence reports non si vede la solution
    /**
     * Change the drone driving mode.
     * @param drivingMode {@link DrivingMode} to set
     */
    public void changeMode(final DrivingMode drivingMode) {
        final ObjectNode jsonNode = new ObjectMapper().createObjectNode();
        final String modeMessage = drivingMode == DrivingMode.AUTOMATIC
                ? MqttMessageValueConstants.AUTOMATIC_MODE_MESSAGE
                : MqttMessageValueConstants.MANUAL_MODE_MESSAGE;
        jsonNode.put(MqttMessageParameterConstants.MODE_PARAMETER, modeMessage);
        Connection.getInstance().publish(MqttTopicConstants.CONTROL_TOPIC + this.orderId, jsonNode);
    }

    /**
     * Makes the drone proceeding.
     */
    public void proceed() {
        final ObjectNode jsonNode = new ObjectMapper().createObjectNode();
        jsonNode.put(MqttMessageParameterConstants.MOVE_PARAMETER, MqttMessageValueConstants.PROCEED_MESSAGE);
        Connection.getInstance().publish(MqttTopicConstants.CONTROL_TOPIC + this.orderId, jsonNode);
    }

    /**
     * Halts the drone.
     */
    public void halt() {
        final ObjectNode jsonNode = new ObjectMapper().createObjectNode();
        jsonNode.put(MqttMessageParameterConstants.MOVE_PARAMETER, MqttMessageValueConstants.HALT_MESSAGE);
        Connection.getInstance().publish(MqttTopicConstants.CONTROL_TOPIC + this.orderId, jsonNode);
    }

    /**
     * Retrieves data history related to the order monitoring.
     * @return the {@link Future} containing the {@link List} of {@link DroneData}
     */
    public Future<List<DroneData>> retrieveDataHistory() {
        return DataRepository.getInstance().retrieveDataHistory(this.orderId);
    }
}
