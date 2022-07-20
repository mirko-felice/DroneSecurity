/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.camera.entities;

import io.github.dronesecurity.dronesystem.drone.application.drone.camera.CameraDataAnalyzerImpl;
import io.github.dronesecurity.dronesystem.drone.application.drone.camera.CameraDataProcessorImpl;
import io.github.dronesecurity.dronesystem.drone.application.drone.camera.CameraDataPublisherImpl;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.drone.application.drone.camera.CameraConnectionImpl;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.CameraAlert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.ProcessedCameraData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.RawCameraData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.services.CameraConnection;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.services.CameraDataAnalyzer;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.services.CameraDataProcessor;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.services.CameraDataPublisher;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.entities.AbstractSensor;

/**
 * Item representing a real camera sensor and observing its values.
 */
public class Camera extends AbstractSensor {

    private RawCameraData rawCameraData;
    private ProcessedCameraData processedCameraData;

    private final CameraConnection cameraConnection;
    private final CameraDataProcessor cameraDataProcessor;
    private final CameraDataAnalyzer cameraDataAnalyzer;
    private final CameraDataPublisher cameraDataPublisher;

    /**
     * Initializes the sensor with its services.
     */
    public Camera() {
        this.cameraConnection = new CameraConnectionImpl();
        this.cameraDataProcessor = new CameraDataProcessorImpl();
        this.cameraDataAnalyzer = new CameraDataAnalyzerImpl();
        this.cameraDataPublisher = new CameraDataPublisherImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getScriptName() {
        return this.isRaspberry() ? "camera" : "cameraSimulator";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deactivate() {
        super.deactivate();
        this.cameraConnection.disconnect();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishData(final OrderData orderData) {
        this.cameraDataPublisher.publishCameraData(orderData, this.processedCameraData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Alert performReading() {
        final Alert alert = super.performReading();
        if (this.processedCameraData == null)
            return alert;
        else
            return new CameraAlert(alert.getAlertType(),
                    alert.getAlertLevel(),
                    this.processedCameraData.getImageLength());
    }

    /**
     * Gets the processed data of the Camera.
     * @return processed camera data of the last reading
     */
    protected RawCameraData getImageData() {
        return this.rawCameraData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readData() {
        this.rawCameraData = this.cameraConnection.readCameraData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processData() {
        this.processedCameraData = this.cameraDataProcessor.processCameraData(this.rawCameraData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Alert analyzeData() {
        return this.cameraDataAnalyzer.analyzeCameraData(this.processedCameraData);
    }
}
