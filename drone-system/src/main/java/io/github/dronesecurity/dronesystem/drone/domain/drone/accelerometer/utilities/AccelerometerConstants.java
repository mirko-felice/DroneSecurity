/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.utilities;

import io.github.dronesecurity.lib.MqttMessageParameterConstants;

/**
 * Accelerometer related strings.
 */
public final class AccelerometerConstants {

    /**
     * Key for the X value.
     */
    public static final String X = "x";

    /**
     * Key for the Y value.
     */
    public static final String Y = "y";

    /**
     * Key for the Z value.
     */
    public static final String Z = "z";

    /**
     * Parameter that contains pitch angle.
     */
    public static final String PITCH = MqttMessageParameterConstants.PITCH;

    /**
     * Parameter that contains roll angle.
     */
    public static final String ROLL = MqttMessageParameterConstants.ROLL;

    /**
     * Parameter that contains yaw angle.
     */
    public static final String YAW = MqttMessageParameterConstants.YAW;

    private AccelerometerConstants() { }
}
