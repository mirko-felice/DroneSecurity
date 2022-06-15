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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;

/**
 * Item representing a real proximity sensor and observing its values.
 */
public class ProximitySensor extends AbstractSensor<Double> {

    private double distance;

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getScriptName() {
        return this.isRaspberry() ? "proximitySensor" : "proximitySimulator";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readData() {
        if (this.isOn() && getOutputStream().size() > 0) {
            final String orig = getOutputStream().toString(StandardCharsets.UTF_8).trim();
            try {
                final JsonNode proximityData = new ObjectMapper().readTree(orig);
                this.distance = proximityData.get(MqttMessageParameterConstants.PROXIMITY_PARAMETER).asDouble();
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
    public Double getData() {
        return BigDecimal.valueOf(this.distance).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }
}
