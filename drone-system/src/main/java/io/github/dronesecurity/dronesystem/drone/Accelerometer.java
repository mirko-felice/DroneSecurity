/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Item representing a real accelerometer sensor and observing its values.
 */
public class Accelerometer extends AbstractSensor<Map<String, Double>> {

    private final Map<String, Double> values = new ConcurrentHashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getScriptName() {
       return this.isRaspberry() ? "accelerometer" : "accelerometerSimulator";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readData() {
        if (this.isOn() && getOutputStream().size() > 0) {
            final String orig = getOutputStream().toString(StandardCharsets.UTF_8).trim();

            final int index = orig.lastIndexOf("\"accelerometer") - 1;

            final String jsonValues = orig.substring(index);

            try {
                final JsonNode accelValues = new ObjectMapper().readTree(jsonValues)
                        .get(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER);
                accelValues.fields().forEachRemaining(k -> this.values.put(k.getKey(), k.getValue().asDouble()));
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(getClass()).error("Can NOT read json correctly.", e);
            }
            getOutputStream().reset();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Double> getData() {
        return Map.copyOf(this.values);
    }
}
