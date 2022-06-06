/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone;


import io.github.dronesecurity.lib.DrivingMode;

import java.util.Map;

/**
 * Item representing a drone with all its physical sensors.
 */
public class Drone {

    private static final SensorFactory SENSOR_FACTORY = new SensorFactory();
    private final Sensor<Double> proximity;
    private final Sensor<Map<String, Double>> accelerometer;
    private final Sensor<Byte[]> camera;
    private boolean isMoving;
    private DrivingMode drivingMode;

    /**
     * Constructs drone's sensors.
     */
    public Drone() {
        this.proximity = SENSOR_FACTORY.getProximitySensor();
        this.accelerometer = SENSOR_FACTORY.getAccelerometer();
        this.camera = SENSOR_FACTORY.getCamera();
        this.drivingMode = DrivingMode.AUTOMATIC;
    }

    /**
    * Executes the analysis of the raw data of all sensors.
    */
    public void readAllData() {
        this.proximity.readData();
        this.accelerometer.readData();
        this.camera.readData();
    }

    /**
     * Gets the proximity sensor data.
     *
     * @return The data read by the proximity sensor of the drone
     */
    public Double getProximitySensorData() {
        return this.proximity.getData();
    }

    /**
     * Gets the accelerometer data.
     *
     * @return The data read by the accelerometer of the drone
    */
    public Map<String, Double> getAccelerometerSensorData() {
        return this.accelerometer.getData();
    }

    /**
     * Gets the camera data.
     *
     * @return The data read by the camera of the drone
    */
    public Byte[] getCameraSensorData() {
        return this.camera.getData();
    }

    /**
     * Activates the Drone, making it operative.
     */
    public void activate() {
        this.proceed();
        this.accelerometer.activate();
        this.camera.activate();
        this.proximity.activate();
    }

    /**
     * Deactivates the Drone, making it inoperative.
     */
    public void deactivate() {
        this.halt();
        this.proximity.deactivate();
        this.accelerometer.deactivate();
        this.camera.deactivate();
    }

    /**
     * Makes the Drone proceed with its delivery.
     */
    public void proceed() {
        if (this.drivingMode == DrivingMode.AUTOMATIC)
            this.isMoving = true;
    }

    /**
     * Halts the Drone.
     */
    public void halt() {
        if (this.drivingMode == DrivingMode.AUTOMATIC)
            this.isMoving = false;
    }

    /**
     * Checks if the Drone is operating (moving).
     * @return true if Drone is moving, false otherwise
     */
    public boolean isOperating() {
        return this.isMoving;
    }

    /**
     * Changes the current driving mode into the new.
     * @param newDrivingMode {@link DrivingMode} to apply
     */
    public void changeMode(final DrivingMode newDrivingMode) {
        this.drivingMode = newDrivingMode;
    }
}
