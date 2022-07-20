/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.negligence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.AccelerometerAlert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.CameraAlert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.ProximityAlert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.lib.DateHelper;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Simple POJO class to represent a negligence report.
 */
public final class NegligenceReport {

    private final String negligent;
    private final ObjectNode data;
    private final long orderId;
    private final Instant negligenceInstant;

    /**
     * Build the report.
     * @param orderData the order data related to this negligence
     * @param proximityAlert the proximity data alert detected by its sensor
     * @param accelerometerAlert the accelerometer data alert detected by its sensor
     * @param cameraAlert the camera data alert detected by its sensor
     */
    public NegligenceReport(final OrderData orderData,
                            final ProximityAlert proximityAlert,
                            final AccelerometerAlert accelerometerAlert,
                            final CameraAlert cameraAlert) {
        this.negligent = orderData.getCourier();
        final ObjectMapper objectMapper = new ObjectMapper();
        this.data = objectMapper.createObjectNode();

        this.data.put(MqttMessageParameterConstants.PROXIMITY_PARAMETER, proximityAlert.getDistance());
        final ObjectNode accelerometerData = objectMapper.createObjectNode();
        accelerometerData.put(MqttMessageParameterConstants.PITCH, accelerometerAlert.getPitch());
        accelerometerData.put(MqttMessageParameterConstants.ROLL, accelerometerAlert.getRoll());
        accelerometerData.put(MqttMessageParameterConstants.YAW, accelerometerAlert.getYaw());
        this.data.set(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER, accelerometerData);
        this.data.put(MqttMessageParameterConstants.CAMERA_PARAMETER, cameraAlert.getImageSize());
        this.orderId = orderData.getOrderId();
        this.negligenceInstant = Instant.now();
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

    /**
     * Gets the {@link Instant} textual representation when negligence has happened.
     * @return the negligence instant string
     */
    public @NotNull String getNegligenceInstant() {
        return DateHelper.toString(this.negligenceInstant);
    }
}
