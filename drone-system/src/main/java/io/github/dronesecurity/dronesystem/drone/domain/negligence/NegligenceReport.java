/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.negligence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.SensorSetAlert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.lib.connection.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.utilities.DateHelper;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Simple POJO class to represent a negligence report.
 */
public final class NegligenceReport {

    private final String negligent;
    private final ObjectNode data;
    private final long orderId;

    /**
     * Build the report.
     * @param orderData the order data related to this negligence
     * @param sensorSetAlert the sensor data alert detected by its sensor set
     */
    public NegligenceReport(final @NotNull OrderData orderData, final @NotNull SensorSetAlert sensorSetAlert) {
        this.negligent = orderData.getCourier();
        final ObjectMapper objectMapper = new ObjectMapper();
        this.data = objectMapper.createObjectNode();

        this.data.put(MqttMessageParameterConstants.PROXIMITY_PARAMETER,
                sensorSetAlert.getProximityAlert().getDistance());
        final ObjectNode accelerometerData = objectMapper.createObjectNode();
        accelerometerData.put(MqttMessageParameterConstants.PITCH, sensorSetAlert.getAccelerometerAlert().getPitch());
        accelerometerData.put(MqttMessageParameterConstants.ROLL, sensorSetAlert.getAccelerometerAlert().getRoll());
        accelerometerData.put(MqttMessageParameterConstants.YAW, sensorSetAlert.getAccelerometerAlert().getYaw());
        this.data.set(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER, accelerometerData);
        this.data.put(MqttMessageParameterConstants.CAMERA_PARAMETER, sensorSetAlert.getCameraAlert().getImageSize());
        this.data.put(MqttMessageParameterConstants.DETECTION_INSTANT, DateHelper.toString(Instant.now()));
        this.orderId = orderData.getOrderId();
    }

    /**
     * Gets the negligent that has to be reported.
     * @return the negligent
     */
    public String getNegligent() {
        return this.negligent;
    }

    /**
     * Gets the collected data as a Json.
     * @return the data
     */
    public ObjectNode getData() {
        return this.data.deepCopy();
    }

    /**
     * Gets the order identifier related to the negligence.
     * @return the order identifier
     */
    public long getOrderId() {
        return this.orderId;
    }
}
