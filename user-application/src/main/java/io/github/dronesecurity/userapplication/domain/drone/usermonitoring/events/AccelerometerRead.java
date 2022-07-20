/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.drone.usermonitoring.events;

import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.AccelerometerData;
import io.github.dronesecurity.userapplication.events.DomainEvent;

/**
 * The event to be raised when the drone publishes its accelerometer sensor data.
 */
public final class AccelerometerRead implements DomainEvent {

    private final AccelerometerData accelerometerData;

    /**
     * Instantiates the Accelerometer data Read event.
     *
     * @param accelerometerData The pitch received from the drone
     */
    public AccelerometerRead(final AccelerometerData accelerometerData) {
        this.accelerometerData = accelerometerData;
    }

    /**
     * Gets the accelerometer data read.
     *
     * @return data read by the accelerometer
     */
    public AccelerometerData getAccelerometerData() {
        return this.accelerometerData;
    }
}
