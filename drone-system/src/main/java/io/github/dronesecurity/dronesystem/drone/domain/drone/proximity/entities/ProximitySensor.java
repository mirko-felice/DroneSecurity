/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.drone.application.drone.proximity.ProximityDataAnalyzerImpl;
import io.github.dronesecurity.dronesystem.drone.application.drone.proximity.ProximityDataProcessorImpl;
import io.github.dronesecurity.dronesystem.drone.application.drone.proximity.ProximityDataPublisherImpl;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.ProximityAlert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.ProcessedProximityData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.RawProximityData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.services.ProximityDataAnalyzer;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.services.ProximityDataProcessor;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.services.ProximityDataPublisher;
import io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.entities.AbstractSensor;
import io.github.dronesecurity.lib.connection.MqttMessageParameterConstants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Item representing a real proximity sensor and observing its values.
 */
public class ProximitySensor extends AbstractSensor {

    private RawProximityData rawProximityData;
    private ProcessedProximityData processedProximityData;

    private final ProximityDataProcessor proximityDataProcessor;
    private final ProximityDataAnalyzer proximityDataAnalyzer;
    private final ProximityDataPublisher proximityDataPublisher;

    /**
     * Initializes the sensor with its services.
     */
    public ProximitySensor() {
        this.proximityDataProcessor = new ProximityDataProcessorImpl();
        this.proximityDataAnalyzer = new ProximityDataAnalyzerImpl();
        this.proximityDataPublisher = new ProximityDataPublisherImpl();
    }

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
    public void publishData(final OrderData orderData) {
        this.proximityDataPublisher.publishProximityData(orderData, this.processedProximityData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Alert performReading() {
        final Alert alert = super.performReading();
        if (this.processedProximityData == null)
            return alert;
        else
            return new ProximityAlert(alert.getAlertType(),
                    alert.getAlertLevel(),
                    this.processedProximityData.getDistance());
    }

    /**
     * Gets the processed data of the Proximity sensor.
     * @return processed proximity sensor data of the last reading
     */
    protected RawProximityData getDistance() {
        return this.rawProximityData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readData() {
        if (this.isOn() && getOutputStream().size() > 0) {
            final String orig = getOutputStream().toString(StandardCharsets.UTF_8).trim();
            try {
                final JsonNode proximityData = new ObjectMapper().readTree(orig);
                this.rawProximityData =
                        new RawProximityData(
                                proximityData.get(MqttMessageParameterConstants.PROXIMITY_PARAMETER).asDouble());
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
    protected void processData() {
        this.processedProximityData = this.proximityDataProcessor.processProximityData(this.rawProximityData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(" -> new")
    protected @NotNull Alert analyzeData() {
        return this.proximityDataAnalyzer.analyzeProximityData(this.processedProximityData);
    }
}
