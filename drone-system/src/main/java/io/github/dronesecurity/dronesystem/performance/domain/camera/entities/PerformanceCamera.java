/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.camera.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.entities.Camera;
import io.github.dronesecurity.dronesystem.performance.application.camera.CameraOutputHelperImpl;
import io.github.dronesecurity.dronesystem.performance.application.camera.CameraPerformancePublisherImpl;
import io.github.dronesecurity.dronesystem.performance.domain.camera.objects.CameraPerformanceData;
import io.github.dronesecurity.dronesystem.performance.domain.camera.services.CameraOutputHelper;
import io.github.dronesecurity.dronesystem.performance.domain.camera.services.CameraPerformancePublisher;
import io.github.dronesecurity.dronesystem.performance.domain.sensor.entities.PerformanceSensor;
import io.github.dronesecurity.dronesystem.performance.utilities.PerformanceStringConstants;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Class representing a {@link Camera} sensor that evaluates its script performance.
 */
public class PerformanceCamera extends Camera implements PerformanceSensor {

    private final CameraOutputHelper outputHelper;
    private final CameraPerformancePublisher publisher;

    private long totalCameraReaderDelay;
    private long totalCameraReadings;


    private long timestamp;
    private int index;
    private CameraPerformanceData cameraData;

    /**
     * Builds the camera that keeps track of its performance data.
     * @throws IOException whether it's impossible to access the file that will contain the performance data
     */
    public PerformanceCamera() throws IOException {
        super();
        this.outputHelper = new CameraOutputHelperImpl();
        this.publisher = new CameraPerformancePublisherImpl();
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
        this.cameraData = new CameraPerformanceData(this.index, this.timestamp, this.getImageData());
        final long delay = System.currentTimeMillis() - this.timestamp;
        this.outputHelper.printCameraPerformance(this.cameraData, delay);
        this.totalCameraReaderDelay += delay;
        this.totalCameraReadings++;
        return alert;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getScriptName() {
        return this.isRaspberry() ? "cameraPerformance" : "cameraPerformanceSimulator";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishPerformanceData() {
        this.publisher.publishCamera(this.cameraData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getAverageDelay() {
        return this.totalCameraReaderDelay / this.totalCameraReadings;
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
            getOutputStream().reset();
        }
    }
}
