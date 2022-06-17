/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.drone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.drone.entities.sensors.Accelerometer;
import io.github.dronesecurity.dronesystem.performance.PerformanceStringConstants;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Class representing an {@link Accelerometer} sensor that evaluates its script performance.
 */
public class AccelerometerTimed extends Accelerometer {

    private long timestamp;
    private int index;

    /**
     * {@inheritDoc}
     */
    @Override
    public void readData() {
        this.readMetadata();
        super.readData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getScriptName() {
        return this.isRaspberry() ? "accelerometerPerformance" : "accelerometerPerformanceSimulator";
    }

    /**
     * Gets the timestamp of the last values read by this accelerometer.
     * @return the timestamp of the last data reading
     */
    public long getReadingTimestamp() {
        return this.timestamp;
    }

    /**
     * Gets the index of the last values read by this accelerometer.
     * @return the index of the last data reading
     */
    public int getReadingIndex() {
        return this.index;
    }

    private void readMetadata() {
        if (this.isOn() && getOutputStream().size() > 0) {
            final String orig = getOutputStream().toString(StandardCharsets.UTF_8).trim();

            try {
                final JsonNode metadata = new ObjectMapper().readTree(orig);
                this.timestamp = metadata.get(PerformanceStringConstants.TIMESTAMP).asLong();
                this.index = metadata.get(PerformanceStringConstants.INDEX).asInt();
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(getClass()).error("Can NOT read json correctly.", e);
            }
        }
    }
}
