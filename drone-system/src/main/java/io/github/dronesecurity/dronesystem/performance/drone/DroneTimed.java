/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.drone;

import io.github.dronesecurity.dronesystem.drone.Drone;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.AccelerometerData;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.CameraData;

/**
 * Class representing a {@link Drone} specifically build to evaluate its performance.
 */
public class DroneTimed extends Drone {

    private final CameraTimed camera;
    private final AccelerometerTimed accelerometer;

    /**
     * Builds the drone.
     */
    public DroneTimed() {
        this.camera = new CameraTimed();
        this.accelerometer = new AccelerometerTimed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readAllData() {
        this.camera.readData();
        this.accelerometer.readData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate() {
        this.camera.activate();
        this.accelerometer.activate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deactivate() {
        this.camera.deactivate();
        this.accelerometer.deactivate();
    }

    /**
     * Gets all performance data of the last camera reading.
     * @return {@link CameraData} containing all the data of the performance evaluation of the camera
     */
    public CameraData getCameraPerformanceData() {

        Byte[] imageBytes = this.camera.getData();
        if (imageBytes == null)
            imageBytes = new Byte[] {};
        return new CameraData(this.camera.getReadingIndex(),
                this.camera.getReadingTimestamp(),
                imageBytes);
    }

    /**
     * Gets all performance data of the last accelerometer reading.
     * @return {@link AccelerometerData} containing all the data of the performance evaluation of the accelerometer
     */
    public AccelerometerData getAccelerometerPerformanceData() {
        return new AccelerometerData(this.accelerometer.getReadingIndex(),
                this.accelerometer.getReadingTimestamp(),
                this.accelerometer.getData());
    }
}
