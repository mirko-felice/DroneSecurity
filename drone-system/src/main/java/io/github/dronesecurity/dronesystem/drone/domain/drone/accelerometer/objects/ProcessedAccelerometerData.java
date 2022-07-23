/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects;

import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.exceptions.NotAcceptableAngleException;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.utilities.AccelerometerConstants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class representing accelerometer data which were transformed to its angle values (roll/pitch/yaw).
 */
public class ProcessedAccelerometerData {

    private static final int MIN_ANGLE = -180;
    private static final int MAX_ANGLE = 180;

    private final int pitch;
    private final int roll;
    private final int yaw;

    /**
     * Builds the processed accelerometer data.
     *
     * @param pitch Pitch angle value
     * @param roll Roll angle value
     * @param yaw Yaw angle value
     */
    public ProcessedAccelerometerData(final int pitch, final int roll, final int yaw) {
        if (pitch < MIN_ANGLE || pitch > MAX_ANGLE
                || roll < MIN_ANGLE || roll > MAX_ANGLE
                || yaw < MIN_ANGLE || yaw > MAX_ANGLE) throw new NotAcceptableAngleException();
        this.pitch = pitch;
        this.roll = roll;
        this.yaw = yaw;
    }

    /**
     * Gets the pitch value of the processed data.
     * @return the pitch angle of the accelerometer
     */
    public int getPitch() {
        return this.pitch;
    }

    /**
     * Gets the roll value of the processed data.
     * @return the roll angle of the accelerometer
     */
    public int getRoll() {
        return this.roll;
    }

    /**
     * Gets the yaw value of the processed data.
     * @return the yaw angle of the accelerometer
     */
    public int getYaw() {
        return this.yaw;
    }

    /**
     * Gets the accelerometer processed data as a map.
     * @return the map with respective pitch/roll/yaw angles
     */
    public Map<String, Integer> asMap() {
        final Map<String, Integer> angles = new ConcurrentHashMap<>();
        angles.put(AccelerometerConstants.PITCH, this.pitch);
        angles.put(AccelerometerConstants.ROLL, this.roll);
        angles.put(AccelerometerConstants.YAW, this.yaw);
        return angles;
    }
}
