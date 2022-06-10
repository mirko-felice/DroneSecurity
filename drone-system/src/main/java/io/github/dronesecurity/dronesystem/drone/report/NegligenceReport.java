/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.lib.DateHelper;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Map;

/**
 * Simple POJO class to represent a negligence report.
 */
public final class NegligenceReport {

    private final String negligent;
    private final ObjectNode data;
    private final String orderId;
    private final Instant negligenceInstant;

    /**
     * Build the report.
     * @param negligent the negligent leading the drone
     * @param proximity the proximity data detected by its sensor
     * @param accelerometer the accelerometer data detected by its sensor
     * @param camera the camera data detected by its sensor
     * @param orderId the order identifier related to this negligence
     */
    public NegligenceReport(final String negligent,
                            final double proximity,
                            final @NotNull Map<String, Double> accelerometer,
                            final Byte @NotNull [] camera,
                            final String orderId) {
        this.negligent = negligent;
        final ObjectMapper objectMapper = new ObjectMapper();
        this.data = objectMapper.createObjectNode();

        this.data.put(MqttMessageParameterConstants.PROXIMITY_PARAMETER, proximity);
        final ObjectNode accelerometerData = objectMapper.createObjectNode();
        accelerometer.forEach(accelerometerData::put);
        this.data.set(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER, accelerometerData);
        this.data.put(MqttMessageParameterConstants.CAMERA_PARAMETER, camera.length);
        this.orderId = orderId;
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
    public String getOrderId() {
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
