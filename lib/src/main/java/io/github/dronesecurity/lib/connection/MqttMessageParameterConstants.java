/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.lib.connection;

import io.github.dronesecurity.lib.shared.AlertLevel;

/**
 * Message JSON parameters.
 */
public final class MqttMessageParameterConstants {

    /**
     * Parameter that contains proximity data.
     */
    public static final String PROXIMITY_PARAMETER = "proximity";

    /**
     * Parameter that contains accelerometer data.
     */
    public static final String ACCELEROMETER_PARAMETER = "accelerometer";

    /**
     * Parameter that contains camera data.
     */
    public static final String CAMERA_PARAMETER = "camera";

    /**
     * Parameter that contains roll angle.
     */
    public static final String ROLL = "roll";

    /**
     * Parameter that contains pitch angle.
     */
    public static final String PITCH = "pitch";

    /**
     * Parameter that contains yaw angle.
     */
    public static final String YAW = "yaw";

    /**
     * Parameter that contains synchronization message.
     */
    public static final String SYNC_PARAMETER = "sync";

    /**
     * Parameter that contains current {@link AlertLevel}.
     */
    public static final String ALERT_LEVEL_PARAMETER = "level";

    /**
     * Parameter that contains alert type.
     */
    public static final String ALERT_TYPE_PARAMETER = "type";

    /**
     * Parameter that contains drone lifecycle updates.
     */
    public static final String STATUS_PARAMETER = "status";

    /**
     * Parameter that contains order.
     */
    public static final String ORDER_PARAMETER = "order";

    /**
     * Parameter that contains order identifier.
     */
    public static final String ORDER_ID_PARAMETER = "id";

    /**
     * Parameter that contains issue report details.
     */
    public static final String ISSUE_REPORT_INFO_PARAMETER = "details";

    /**
     * Parameter that contains courier leading the order.
     */
    public static final String COURIER_PARAMETER = "courier";

    /**
     * Parameter that contains detection instant for a Negligence Report.
     */
    public static final String DETECTION_INSTANT = "detectionInstant";

    /**
     * Parameter that contains new guide mode for the drone.
     */
    public static final String MODE_PARAMETER = "mode";

    /**
     * Parameter that contains the move parameter for the drone.
     */
    public static final String MOVE_PARAMETER = "move";

    /**
     * Parameter that contains the moving state parameter of the drone.
     */
    public static final String DRONE_MOVING_STATE_PARAMETER = "movingState";

    private MqttMessageParameterConstants() { }
}
