/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.drone.application.drone.accelerometer.AccelerometerDataAnalyzerImpl;
import io.github.dronesecurity.dronesystem.drone.application.drone.accelerometer.AccelerometerDataProcessorImpl;
import io.github.dronesecurity.dronesystem.drone.application.drone.accelerometer.AccelerometerDataPublisherImpl;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.ProcessedAccelerometerData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.RawAccelerometerData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.services.AccelerometerDataAnalyzer;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.services.AccelerometerDataProcessor;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.services.AccelerometerDataPublisher;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.AccelerometerAlert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.entities.AbstractSensor;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.utilities.AccelerometerConstants;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Item representing a real accelerometer sensor and observing its values.
 */
public class Accelerometer extends AbstractSensor {

    private RawAccelerometerData accelerometerData;
    private ProcessedAccelerometerData processedData;

    private final AccelerometerDataProcessor accelerometerDataProcessor;
    private final AccelerometerDataAnalyzer accelerometerDataAnalyzer;
    private final AccelerometerDataPublisher accelerometerDataPublisher;

    /**
     * Initializes the sensor with its services.
     */
    public Accelerometer() {
        this.accelerometerDataProcessor = new AccelerometerDataProcessorImpl();
        this.accelerometerDataAnalyzer = new AccelerometerDataAnalyzerImpl();
        this.accelerometerDataPublisher = new AccelerometerDataPublisherImpl();
    }

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
    public void publishData(final OrderData orderData) {
        this.accelerometerDataPublisher.publishAccelerometerData(orderData, this.processedData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Alert performReading() {
        final Alert alert = super.performReading();
        if (this.processedData == null)
            return alert;
        else
            return new AccelerometerAlert(alert.getAlertType(),
                    alert.getAlertLevel(),
                    this.processedData.getPitch(),
                    this.processedData.getRoll(),
                    this.processedData.getYaw());
    }

    /**
     * Gets the processed data of the Accelerometer.
     * @return processed accelerometer data of the last reading
     */
    protected ProcessedAccelerometerData getProcessedData() {
        return this.processedData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readData() {
        if (this.isOn() && getOutputStream().size() > 0) {
            final String orig = getOutputStream().toString(StandardCharsets.UTF_8).trim();

            final int index = orig.lastIndexOf("\"accelerometer") - 1;

            final String jsonValues = orig.substring(index);

            try {
                final JsonNode accelValues = new ObjectMapper().readTree(jsonValues)
                        .get(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER);
                this.accelerometerData = new RawAccelerometerData(
                        accelValues.get(AccelerometerConstants.X).asDouble(),
                        accelValues.get(AccelerometerConstants.Y).asDouble(),
                        accelValues.get(AccelerometerConstants.Z).asDouble());
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
        this.processedData = this.accelerometerDataProcessor.processAccelerometerData(this.accelerometerData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Alert analyzeData() {
        return this.accelerometerDataAnalyzer.analyzeAccelerometerData(this.processedData);
    }
}
