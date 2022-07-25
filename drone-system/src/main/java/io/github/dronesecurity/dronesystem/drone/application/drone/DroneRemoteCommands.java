/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.application.drone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.drone.domain.drone.drone.entities.Drone;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.lib.connection.Connection;
import io.github.dronesecurity.lib.connection.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.connection.MqttMessageValueConstants;
import io.github.dronesecurity.lib.connection.MqttTopicConstants;
import io.github.dronesecurity.lib.shared.DrivingMode;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.crt.mqtt.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * Service that provides the possibility to control the drone with remote commands through the {@link Connection}.
 */
public class DroneRemoteCommands {

    private static final String JSON_ERROR_MESSAGE = "Can NOT read json correctly.";
    private final Drone drone;
    private final CountDownLatch latch;

    private NavigationService navigationService;
    private DataSharingService dataSharingService;
    private OrderData orderData;

    /**
     * Builds the service instantiating the drone to operate on.
     */
    public DroneRemoteCommands() {
        this.drone = new Drone(Connection.getInstance().getIdentifier());
        this.latch = new CountDownLatch(1);
    }

    //TODO refactor all value objects
    /**
     * Starts the service by waiting for the remote application to send the starting signal.
     */
    public void waitForActivation() {
        Connection.getInstance().subscribe(MqttTopicConstants.ORDER_TOPIC + this.drone.getId(), msg -> {
            try {
                final JsonNode json = new ObjectMapper().readTree(new String(msg.getPayload(), StandardCharsets.UTF_8));
                if (MqttMessageValueConstants.PERFORM_DELIVERY_MESSAGE
                        .equals(json.get(MqttMessageParameterConstants.SYNC_PARAMETER).asText())) {

                    final long currentOrderId = json.get(MqttMessageParameterConstants.ORDER_PARAMETER)
                            .get(MqttMessageParameterConstants.ORDER_ID_PARAMETER).asLong();
                    final String currentCourier = json.get(MqttMessageParameterConstants.COURIER_PARAMETER).asText();

                    this.orderData = new OrderData(currentOrderId, currentCourier);

                    this.drone.activate(this.orderData);
                    this.navigationService = new NavigationService(this.drone, this.orderData);
                    this.navigationService.start();
                    this.dataSharingService = new DataSharingService(this.drone);
                    this.dataSharingService.start();

                    this.activateTravelControls();
                    this.latch.countDown();
                }
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(getClass()).error(JSON_ERROR_MESSAGE, e);
            }
        });
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LoggerFactory.getLogger(getClass()).error("Thread interrupted", e);
        }
    }

    private void stopDrone() {
        this.dataSharingService.stop();
        this.drone.deactivate();
        Connection.getInstance().closeConnection();
    }

    private void activateTravelControls() {
        final Connection connection = Connection.getInstance();
        connection.subscribe(MqttTopicConstants.CONTROL_TOPIC + this.orderData.getOrderId(),
                this::control);
        connection.unsubscribe(MqttTopicConstants.ORDER_TOPIC + this.drone.getId());

        connection.subscribe(MqttTopicConstants.ORDER_TOPIC + this.orderData.getOrderId(), msg -> {
            try {
                final JsonNode json = new ObjectMapper().readTree(new String(msg.getPayload(), StandardCharsets.UTF_8));
                if (MqttMessageValueConstants.DRONE_CALLBACK_MESSAGE
                        .equals(json.get(MqttMessageParameterConstants.SYNC_PARAMETER).asText())) {
                    this.drone.proceed();
                    this.navigationService.callback(this::stopDrone);
                }
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(getClass()).error(JSON_ERROR_MESSAGE, e);
            }
        });
    }

    private void control(final @NotNull MqttMessage message) {
        try {
            final JsonNode json = new ObjectMapper().readTree(new String(message.getPayload(), StandardCharsets.UTF_8));
            if (json.has(MqttMessageParameterConstants.MODE_PARAMETER)) {
                final String mode = json.get(MqttMessageParameterConstants.MODE_PARAMETER).asText();
                if (MqttMessageValueConstants.AUTOMATIC_MODE_MESSAGE.equals(mode))
                    this.drone.changeMode(DrivingMode.AUTOMATIC);
                else if (MqttMessageValueConstants.MANUAL_MODE_MESSAGE.equals(mode)) {
                    this.drone.changeMode(DrivingMode.MANUAL);
                }
            } else if (json.has(MqttMessageParameterConstants.MOVE_PARAMETER)) {
                final String move = json.get(MqttMessageParameterConstants.MOVE_PARAMETER).asText();
                if (MqttMessageValueConstants.PROCEED_MESSAGE.equals(move)) {
                    this.drone.proceed();
                } else if (MqttMessageValueConstants.HALT_MESSAGE.equals(move)) {
                    this.drone.halt();
                }
            }
        } catch (JsonProcessingException e) {
            LoggerFactory.getLogger(getClass()).error(JSON_ERROR_MESSAGE, e);
        }
    }
}
