/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.lib.connection;

/**
 * Message exchange topic names.
 */
public final class MqttTopicConstants {

    /**
     * Sensor data exchange topic name.
     */
    public static final String DATA_TOPIC = "data";

    /**
     * Current alert level topic name.
     */
    public static final String ALERT_LEVEL_TOPIC = "alertLevel";

    /**
     * Drone System and User Application synchronization topic name.
     */
    public static final String ORDER_TOPIC = "sync";

    /**
     * Drone System and User Application synchronization topic name.
     */
    public static final String LIFECYCLE_TOPIC = "lifecycle";

    /**
     * Drone moving state change topic name.
     */
    public static final String DRONE_MOVING_TOPIC = "movingStateChange";

    /**
     * Issue report related topic name.
     */
    public static final String ISSUE_TOPIC = "issue";

    /**
     * Negligence reports topic name.
     */
    public static final String NEGLIGENCE_REPORTS_TOPIC = "negligenceReports";

    /**
     * Drone's performance data topic name.
     */
    public static final String PERFORMANCE_TOPIC = "dronePerformance";

    /**
     * Drone controlling topic name.
     */
    public static final String CONTROL_TOPIC = "control";

    private MqttTopicConstants() { }
}
