/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.drone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.drone.Accelerometer;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
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
        return "accelerometerPerformanceSimulator";
    }


    /**
     * Gets the timestamp of the last frame read by this camera.
     * @return the timestamp of the last frame
     */
    public long getReadingTimestamp() {
        return this.timestamp;
    }

    /**
     * Gets the index of the last frame read by this camera.
     * @return the index of the last frame
     */
    public int getReadingIndex() {
        return this.index;
    }

    private void readMetadata() {
        if (getOutputStream().size() > 0) {
            final String orig = getOutputStream().toString(StandardCharsets.UTF_8).trim();

            try {
                final JsonNode metadata = new ObjectMapper().readTree(orig);
                this.timestamp = metadata.get(MqttMessageParameterConstants.TIMESTAMP).asLong();
                this.index = metadata.get(MqttMessageParameterConstants.INDEX).asInt();
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(getClass()).error("Can NOT read json correctly.", e);
            }
        }
    }
}
