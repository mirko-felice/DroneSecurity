/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.negligence.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.userapplication.reporting.negligence.utilities.NegligenceConstants;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link DroneData}.
 */
public class DroneDataImpl implements DroneData {

    private final JsonNode copy;
    private Double proximity;
    private final Map<String, Double> accelerometer;
    private Long camera;

    /**
     * Build an empty Drone Data.
     */
    public DroneDataImpl() {
        this(new ObjectMapper().createObjectNode());
    }

    /**
     * Build the Object starting from data.
     * @param data object to retrieve data from
     */
    public DroneDataImpl(final @NotNull JsonNode data) {
        this.copy = data.deepCopy();

        if (data.has(NegligenceConstants.PROXIMITY))
            this.proximity = data.get(NegligenceConstants.PROXIMITY).asDouble();
        this.accelerometer = new HashMap<>();
        if (data.has(NegligenceConstants.ACCELEROMETER))
            data.get(NegligenceConstants.ACCELEROMETER).fields().forEachRemaining(entry ->
                this.accelerometer.put(entry.getKey(), entry.getValue().asDouble()));
        if (data.has(NegligenceConstants.CAMERA))
            this.camera = data.get(NegligenceConstants.CAMERA).asLong();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getProximity() {
        return this.proximity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Double> getAccelerometer() {
        return Map.copyOf(this.accelerometer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getCamera() {
        return this.camera;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DroneData deepCopy() {
        return new DroneDataImpl(this.copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return this.copy.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Sensor Data (click for details)";
    }
}
