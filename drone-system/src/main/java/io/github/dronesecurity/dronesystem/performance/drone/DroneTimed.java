/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.drone;

import io.github.dronesecurity.dronesystem.drone.Drone;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.AccelerometerData;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.CameraData;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.ProximityData;

/**
 * Class representing a {@link Drone} specifically build to evaluate its performance.
 */
public class DroneTimed extends Drone {

    private final CameraTimed camera;
    private final AccelerometerTimed accelerometer;
    private final ProximityTimed proximity;

    /**
     * Builds the drone.
     * @param id drone identifier
     */
    public DroneTimed(final String id) {
        super(id);
        this.camera = new CameraTimed();
        this.accelerometer = new AccelerometerTimed();
        this.proximity = new ProximityTimed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readAllData() {
        this.camera.readData();
        this.accelerometer.readData();
        this.proximity.readData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate() {
        this.camera.activate();
        this.accelerometer.activate();
        this.proximity.activate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deactivate() {
        this.camera.deactivate();
        this.accelerometer.deactivate();
        this.proximity.deactivate();
    }

    /**
     * Gets all performance data of the last camera reading.
     * @return {@link CameraData} containing all the data of the performance evaluation of the camera
     */
    public CameraData getCameraPerformanceData() {
        return new CameraData(this.camera.getReadingIndex(),
                this.camera.getReadingTimestamp(),
                this.camera.getData().length);
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

    /**
     * Gets all performance data of the last proximity sensor reading.
     * @return {@link ProximityData} containing all the data of the performance evaluation of the proximity sensor
     */
    public ProximityData getProximityPerformanceData() {
        return new ProximityData(this.proximity.getReadingIndex(),
                this.proximity.getReadingTimestamp(),
                this.proximity.getData());
    }
}
