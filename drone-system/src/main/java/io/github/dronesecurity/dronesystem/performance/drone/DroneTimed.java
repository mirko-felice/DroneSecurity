/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.drone;

import io.github.dronesecurity.dronesystem.drone.Drone;

/**
 * Class representing a {@link Drone} specifically build to evaluate its performance.
 */
public class DroneTimed extends Drone {

    private final CameraTimed camera;

    /**
     * Builds the drone.
     */
    public DroneTimed() {
        this.camera = new CameraTimed();
        this.camera.activate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readAllData() {
        this.camera.readData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte[] getCameraSensorData() {
        return this.camera.getData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activate() {
        // Overrides original activation method to avoid activating unwanted sensors.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deactivate() {
        this.camera.deactivate();
    }

    /**
     * Gets the timestamp of the last frame read by the drone's camera.
     * @return the timestamp of the last frame
     */
    public long getCameraReadingTimestamp() {
        return this.camera.getReadingTimestamp();
    }

    /**
     * Gets the index of the last frame read by the drone's camera.
     * @return the index of the last frame
     */
    public int getCameraReadingIndex() {
        return this.camera.getReadingIndex();
    }
}
