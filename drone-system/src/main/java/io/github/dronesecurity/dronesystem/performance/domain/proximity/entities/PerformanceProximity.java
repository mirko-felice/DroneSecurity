/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.proximity.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.entities.ProximitySensor;
import io.github.dronesecurity.dronesystem.performance.application.proximity.ProximityOutputHelperImpl;
import io.github.dronesecurity.dronesystem.performance.application.proximity.ProximityPerformancePublisherImpl;
import io.github.dronesecurity.dronesystem.performance.domain.proximity.objects.ProximityPerformanceData;
import io.github.dronesecurity.dronesystem.performance.domain.proximity.services.ProximityOutputHelper;
import io.github.dronesecurity.dronesystem.performance.domain.proximity.services.ProximityPerformancePublisher;
import io.github.dronesecurity.dronesystem.performance.domain.sensor.entities.PerformanceSensor;
import io.github.dronesecurity.dronesystem.performance.utilities.PerformanceStringConstants;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Class representing a {@link ProximitySensor} that evaluates its script performance.
 */
public class PerformanceProximity extends ProximitySensor implements PerformanceSensor {

    private final ProximityOutputHelper outputHelper;
    private final ProximityPerformancePublisher publisher;

    private long totalProximityReaderDelay;
    private long totalProximityReadings;

    private long timestamp;
    private int index;
    private ProximityPerformanceData proximityData;

    /**
     * Builds the proximity sensor that keeps track of its performance data.
     * @throws IOException whether it's impossible to access the file that will contain the performance data
     */
    public PerformanceProximity() throws IOException {
        super();
        this.outputHelper = new ProximityOutputHelperImpl();
        this.publisher = new ProximityPerformancePublisherImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deactivate() {
        this.outputHelper.flushAndClose();
        super.deactivate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Alert performReading() {
        this.readMetadata();
        final Alert alert = super.performReading();

        this.proximityData = new ProximityPerformanceData(this.index, this.timestamp, this.getDistance());
        final long delay = System.currentTimeMillis() - this.timestamp;
        this.outputHelper.printProximityPerformance(this.proximityData, delay);
        this.totalProximityReaderDelay += delay;
        this.totalProximityReadings++;

        return alert;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getScriptName() {
        return this.isRaspberry() ? "proximityPerformance" : "proximityPerformanceSimulator";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishPerformanceData() {
        this.publisher.publishProximity(this.proximityData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getAverageDelay() {
        return this.totalProximityReaderDelay / this.totalProximityReadings;
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
