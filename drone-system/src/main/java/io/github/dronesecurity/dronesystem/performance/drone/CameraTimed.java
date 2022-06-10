/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.drone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.drone.Camera;
import io.github.dronesecurity.dronesystem.performance.PerformanceStringConstants;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Class representing a {@link Camera} sensor that evaluates its script performance.
 */
public class CameraTimed extends Camera {

    private long timestamp;
    private int index;

    /**
     * {@inheritDoc}
     */
    @Override
    public void readData() {
        super.readData();
        this.readMetadata();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getScriptName() {
        return "cameraPerformance";
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
                this.timestamp = metadata.get(PerformanceStringConstants.TIMESTAMP).asLong();
                this.index = metadata.get(PerformanceStringConstants.INDEX).asInt();
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(getClass()).error("Can NOT read json correctly.", e);
            }
            getOutputStream().reset();
        }
    }
}
