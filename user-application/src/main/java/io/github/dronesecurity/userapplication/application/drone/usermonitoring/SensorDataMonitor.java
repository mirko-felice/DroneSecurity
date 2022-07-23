/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.drone.usermonitoring;

import io.github.dronesecurity.lib.connection.Connection;
import io.github.dronesecurity.lib.shared.Date;
import io.github.dronesecurity.lib.connection.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.connection.MqttTopicConstants;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.events.AccelerometerRead;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.events.CameraRead;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.events.ProximityRead;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.AccelerometerData;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.CameraData;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.ProximityData;
import io.github.dronesecurity.userapplication.events.DomainEvents;
import io.vertx.core.json.JsonObject;

import java.nio.charset.StandardCharsets;

/**
 * Helper that tracks sensor data published by the drone delivering an order.
 */
public final class SensorDataMonitor {

    private static final String SPACE_SEPARATOR = " ";

    private SensorDataMonitor() { }

    /**
     * Starts monitoring sensor data related to an order.
     * @param orderId order identifier to track data from
     */
    public static void startSensorDataMonitoring(final long orderId) {
        subscribeToProximityDataRead(orderId);
        subscribeToAccelerometerDataRead(orderId);
        subscribeToCameraDataRead(orderId);
    }

    /**
     * Stops monitoring sensor data related to an order.
     * @param orderId order identifier to track data from
     */
    public static void stopSensorDataMonitoring(final long orderId) {
        final Connection connection = Connection.getInstance();
        connection.unsubscribe(MqttTopicConstants.DATA_TOPIC + orderId + SPACE_SEPARATOR
                + MqttMessageParameterConstants.PROXIMITY_PARAMETER);
        connection.unsubscribe(MqttTopicConstants.DATA_TOPIC + orderId + SPACE_SEPARATOR
                + MqttMessageParameterConstants.ACCELEROMETER_PARAMETER);
        connection.unsubscribe(MqttTopicConstants.DATA_TOPIC + orderId + SPACE_SEPARATOR
                + MqttMessageParameterConstants.CAMERA_PARAMETER);
    }

    private static void subscribeToProximityDataRead(final long orderId) {
        Connection.getInstance().subscribe(MqttTopicConstants.DATA_TOPIC + orderId + SPACE_SEPARATOR
                + MqttMessageParameterConstants.PROXIMITY_PARAMETER, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload(), StandardCharsets.UTF_8));
            final double proximity = json.getDouble(MqttMessageParameterConstants.PROXIMITY_PARAMETER);

            DomainEvents.raise(new ProximityRead(new ProximityData(Date.now(), orderId, proximity)));
        });
    }

    private static void subscribeToAccelerometerDataRead(final long orderId) {
        Connection.getInstance().subscribe(MqttTopicConstants.DATA_TOPIC + orderId + SPACE_SEPARATOR
                + MqttMessageParameterConstants.ACCELEROMETER_PARAMETER, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload(), StandardCharsets.UTF_8));

            final JsonObject accelerometerJson =
                    json.getJsonObject(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER);

            final int pitch = accelerometerJson.getInteger(MqttMessageParameterConstants.PITCH);
            final int roll = accelerometerJson.getInteger(MqttMessageParameterConstants.ROLL);
            final int yaw = accelerometerJson.getInteger(MqttMessageParameterConstants.YAW);

            DomainEvents.raise(new AccelerometerRead(new AccelerometerData(Date.now(), orderId, pitch, roll, yaw)));
        });
    }

    private static void subscribeToCameraDataRead(final long orderId) {
        Connection.getInstance().subscribe(MqttTopicConstants.DATA_TOPIC + orderId + SPACE_SEPARATOR
                + MqttMessageParameterConstants.CAMERA_PARAMETER, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload(), StandardCharsets.UTF_8));

            final int camera = json.getInteger(MqttMessageParameterConstants.CAMERA_PARAMETER);

            DomainEvents.raise(new CameraRead(new CameraData(Date.now(), orderId, camera)));
        });
    }
}
