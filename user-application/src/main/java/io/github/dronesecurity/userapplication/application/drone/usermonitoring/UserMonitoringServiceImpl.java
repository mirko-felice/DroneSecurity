/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.drone.usermonitoring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.lib.connection.Connection;
import io.github.dronesecurity.lib.connection.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.connection.MqttTopicConstants;
import io.github.dronesecurity.lib.shared.AlertLevel;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.events.*;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.repo.DataRepository;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.services.UserMonitoringService;
import io.github.dronesecurity.userapplication.events.DomainEvents;
import io.vertx.core.json.JsonObject;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Implementation of {@link UserMonitoringService}.
 */
public final class UserMonitoringServiceImpl implements UserMonitoringService {

    private static final String JSON_ERROR_MESSAGE = "Can NOT read json correctly.";
    private final DataRepository dataRepository;

    /**
     * Build the service.
     * @param dataRepository {@link DataRepository} to save data into
     */
    public UserMonitoringServiceImpl(final DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        DomainEvents.register(ProximityRead.class, event ->
                this.dataRepository.saveProximitySnapshot(event.getProximity()));
        DomainEvents.register(AccelerometerRead.class, event ->
                this.dataRepository.saveAccelerometerSnapshot(event.getAccelerometerData()));
        DomainEvents.register(CameraRead.class, event ->
                this.dataRepository.saveCameraSnapshot(event.getCameraData()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startOrderMonitoring(final long orderId) {
        SensorDataMonitor.startSensorDataMonitoring(orderId);
        this.subscribeToAlerts(orderId);
        this.subscribeToOrderStatusChange(orderId);
        this.subscribeToMovingStateChange(orderId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopOrderMonitoring(final long orderId) {
        SensorDataMonitor.stopSensorDataMonitoring(orderId);
        final Connection connection = Connection.getInstance();
        connection.unsubscribe(MqttTopicConstants.ALERT_LEVEL_TOPIC + orderId);
        connection.unsubscribe(MqttTopicConstants.LIFECYCLE_TOPIC + orderId);
        connection.unsubscribe(MqttTopicConstants.DRONE_MOVING_TOPIC + orderId);
    }

    private void subscribeToAlerts(final long orderId) {
        Connection.getInstance().subscribe(MqttTopicConstants.ALERT_LEVEL_TOPIC + orderId, msg -> {
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

    private void subscribeToOrderStatusChange(final long orderId) {
        Connection.getInstance().subscribe(MqttTopicConstants.LIFECYCLE_TOPIC + orderId, msg -> {
            try {
                final JsonNode json = new ObjectMapper().readTree(new String(msg.getPayload(), StandardCharsets.UTF_8));
                final String status = json.get(MqttMessageParameterConstants.STATUS_PARAMETER).asText();
                DomainEvents.raise(new StatusChanged(status));
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(UserMonitoringServiceImpl.class).error(JSON_ERROR_MESSAGE, e);
            }
        });
    }

    private void subscribeToMovingStateChange(final long orderId) {
        Connection.getInstance().subscribe(MqttTopicConstants.DRONE_MOVING_TOPIC + orderId, msg -> {
            try {
                final JsonNode json = new ObjectMapper().readTree(new String(msg.getPayload(), StandardCharsets.UTF_8));
                final String movingState =
                        json.get(MqttMessageParameterConstants.DRONE_MOVING_STATE_PARAMETER).asText();
                DomainEvents.raise(new MovingStateChanged(movingState));
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(UserMonitoringServiceImpl.class).error(JSON_ERROR_MESSAGE, e);
            }
        });
    }

}
