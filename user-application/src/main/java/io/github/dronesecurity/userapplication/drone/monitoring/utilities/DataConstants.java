/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.drone.monitoring.utilities;

import io.github.dronesecurity.lib.MqttMessageParameterConstants;

/**
 * Data related strings.
 */
public final class DataConstants {

    /**
     * Key for proximity data.
     */
    public static final String PROXIMITY = "proximity";

    /**
     * Key for accelerometer data.
     */
    public static final String ACCELEROMETER = "accelerometer";

    /**
     * Key for camera data.
     */
    public static final String CAMERA = "camera";

    /**
     * Key for detection instant.
     */
    public static final String DETECTION_INSTANT = "detectionInstant";

    /**
     * Key for the order identifier.
     */
    public static final String ORDER_ID = "orderId";

    /**
     * Key for the roll angle.
     */
    public static final String ROLL = MqttMessageParameterConstants.ROLL;

    /**
     * Key for the pitch angle.
     */
    public static final String PITCH = MqttMessageParameterConstants.PITCH;

    /**
     * Key for the yaw angle.
     */
    public static final String YAW = MqttMessageParameterConstants.YAW;

    private DataConstants() { }
}
