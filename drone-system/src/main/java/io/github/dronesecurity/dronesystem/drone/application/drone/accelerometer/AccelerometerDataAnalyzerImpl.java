/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.application.drone.accelerometer;

import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.ProcessedAccelerometerData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.services.AccelerometerDataAnalyzer;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.lib.AlertLevel;
import io.github.dronesecurity.lib.AlertType;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link AccelerometerDataAnalyzer} informs of the {@link AlertLevel}
 * depending on the angles detected by the accelerometer.
 */
public class AccelerometerDataAnalyzerImpl implements AccelerometerDataAnalyzer {

    private static final double ACCELEROMETER_CRITICAL_THRESHOLD = 45;
    private static final double ACCELEROMETER_WARNING_THRESHOLD = 30;

    /**
     * {@inheritDoc}
     */
    @Override
    public Alert analyzeAccelerometerData(final @NotNull ProcessedAccelerometerData accelerometerData) {
        final int roll = accelerometerData.getRoll();
        final int pitch = accelerometerData.getPitch();
        // Yaw angle is not analyzed because of its uselessness in this context

        final boolean isWarning = Math.abs(roll) > ACCELEROMETER_WARNING_THRESHOLD
                || Math.abs(pitch) > ACCELEROMETER_WARNING_THRESHOLD;

        final boolean isCritical = Math.abs(roll) > ACCELEROMETER_CRITICAL_THRESHOLD
                || Math.abs(pitch) > ACCELEROMETER_CRITICAL_THRESHOLD;

        if (isCritical)
            return new Alert(AlertType.ANGLE, AlertLevel.CRITICAL);
        else if (isWarning)
            return new Alert(AlertType.ANGLE, AlertLevel.WARNING);
        return new Alert(AlertType.ANGLE, AlertLevel.STABLE);
    }
}
