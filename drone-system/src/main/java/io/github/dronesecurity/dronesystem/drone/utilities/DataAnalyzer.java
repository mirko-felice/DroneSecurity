/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.utilities;

import io.github.dronesecurity.dronesystem.drone.entities.sensors.Accelerometer;
import io.github.dronesecurity.dronesystem.drone.entities.sensors.ProximitySensor;
import io.github.dronesecurity.lib.AlertLevel;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Sensor Data analyzer to check critical or warning situations.
 */
public final class DataAnalyzer {

    private static final double PROXIMITY_WARNING_THRESHOLD = 50;
    private static final double PROXIMITY_CRITICAL_THRESHOLD = 25;
    private static final double ACCELEROMETER_CRITICAL_THRESHOLD = 45;
    private static final double ACCELEROMETER_WARNING_THRESHOLD = 30;

    private DataAnalyzer() { }

    /**
     * Checks if proximity distance is critical or warning giving back an {@link AlertLevel}.
     * @param proximityDistance distance collected by the {@link ProximitySensor}
     * @return an {@link AlertLevel}
     */
    public static AlertLevel checkProximitySensorDataAlertLevel(final Double proximityDistance) {
        if (proximityDistance > 0) {
            final boolean isWarning = proximityDistance <= PROXIMITY_WARNING_THRESHOLD;
            final boolean isCritical = proximityDistance <= PROXIMITY_CRITICAL_THRESHOLD;
            if (isCritical)
                return AlertLevel.CRITICAL;
            else if (isWarning)
                return AlertLevel.WARNING;
        }
        return AlertLevel.STABLE;
    }

    /**
     * Checks if inclination angle is critical or warning giving back an {@link AlertLevel}.
     * @param accelerometerData map of data collected by the {@link Accelerometer}
     * @return an {@link AlertLevel}
     */
    public static AlertLevel checkAccelerometerDataAlertLevel(final @NotNull Map<String, Integer> accelerometerData) {
        if (!accelerometerData.isEmpty()) {
            final int roll = accelerometerData.get(MqttMessageParameterConstants.ROLL);
            final int pitch = accelerometerData.get(MqttMessageParameterConstants.PITCH);
            // Yaw angle is not analyzed because of its uselessness in this context

            final boolean isWarning = Math.abs(roll) > ACCELEROMETER_WARNING_THRESHOLD
                                    || Math.abs(pitch) > ACCELEROMETER_WARNING_THRESHOLD;

            final boolean isCritical = Math.abs(roll) > ACCELEROMETER_CRITICAL_THRESHOLD
                                    || Math.abs(pitch) > ACCELEROMETER_CRITICAL_THRESHOLD;

            if (isCritical)
                return AlertLevel.CRITICAL;
            else if (isWarning)
                return AlertLevel.WARNING;
        }
        return AlertLevel.STABLE;
    }

}
