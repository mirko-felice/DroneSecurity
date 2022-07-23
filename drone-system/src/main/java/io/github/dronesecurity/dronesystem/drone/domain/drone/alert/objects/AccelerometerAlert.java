/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects;

import io.github.dronesecurity.lib.shared.AlertLevel;
import io.github.dronesecurity.lib.shared.AlertType;

/**
 * Alert representing the alert specific to the accelerometer.
 */
public class AccelerometerAlert extends Alert {

    private final int pitch;
    private final int roll;
    private final int yaw;

    /**
     * Builds the Alert with its own type and level.
     * @param alertType The type of the alert
     * @param alertLevel The gravity of the alert
     * @param pitch The pitch angle detected by the accelerometer sensor
     * @param roll The roll angle detected by the accelerometer sensor
     * @param yaw The yaw angle detected by the accelerometer sensor
     */
    public AccelerometerAlert(final AlertType alertType,
                              final AlertLevel alertLevel,
                              final int pitch,
                              final int roll,
                              final int yaw) {
        super(alertType, alertLevel);
        this.pitch = pitch;
        this.roll = roll;
        this.yaw = yaw;
    }

    /**
     * Gets the pitch angle related to the alert.
     * @return The pitch angle of the alert
     */
    public int getPitch() {
        return this.pitch;
    }

    /**
     * Gets the roll angle related to the alert.
     * @return The roll angle of the alert
     */
    public int getRoll() {
        return this.roll;
    }

    /**
     * Gets the yaw angle related to the alert.
     * @return The yaw angle of the alert
     */
    public int getYaw() {
        return this.yaw;
    }
}
