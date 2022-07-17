/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.accelerometer.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.entities.Accelerometer;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.performance.application.accelerometer.AccelerometerOutputHelperImpl;
import io.github.dronesecurity.dronesystem.performance.application.accelerometer.AccelerometerPerformancePublisherImpl;
import io.github.dronesecurity.dronesystem.performance.domain.accelerometer.objects.ProcessedAccelerometerPerformanceData;
import io.github.dronesecurity.dronesystem.performance.domain.accelerometer.services.AccelerometerOutputHelper;
import io.github.dronesecurity.dronesystem.performance.domain.accelerometer.services.AccelerometerPerformancePublisher;
import io.github.dronesecurity.dronesystem.performance.domain.sensor.entities.PerformanceSensor;
import io.github.dronesecurity.dronesystem.performance.utilities.PerformanceStringConstants;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Class representing an {@link Accelerometer} sensor that evaluates its script performance.
 */
public class PerformanceAccelerometer extends Accelerometer implements PerformanceSensor {


    private final AccelerometerOutputHelper outputHelper;
    private final AccelerometerPerformancePublisher publisher;

    private long totalAccelerometerReaderDelay;
    private long totalAccelerometerReadings;

    private long timestamp;
    private int index;
    private ProcessedAccelerometerPerformanceData accelerometerData;

    /**
     * Builds the accelerometer that keeps track of its performance data.
     * @throws IOException whether it's impossible to access the file that will contain the performance data
     */
    public PerformanceAccelerometer() throws IOException {
        super();
        this.outputHelper = new AccelerometerOutputHelperImpl();
        this.publisher = new AccelerometerPerformancePublisherImpl();
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
        final Alert alert = super.performReading();
        this.readMetadata();
        this.accelerometerData =
                new ProcessedAccelerometerPerformanceData(this.index, this.timestamp, this.getProcessedData());
        final long delay = System.currentTimeMillis() - this.timestamp;
        this.outputHelper.printAccelerometerPerformance(this.accelerometerData, delay);
        this.totalAccelerometerReaderDelay += delay;
        this.totalAccelerometerReadings++;
        return alert;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getScriptName() {
        return this.isRaspberry() ? "accelerometerPerformance" : "accelerometerPerformanceSimulator";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishPerformanceData() {
        this.publisher.publishAccelerometer(this.accelerometerData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getAverageDelay() {
        return this.totalAccelerometerReaderDelay / this.totalAccelerometerReadings;
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
