/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.analyze;

import io.github.dronesecurity.dronesystem.drone.application.drone.accelerometer.AccelerometerDataAnalyzerImpl;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.ProcessedAccelerometerData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.services.AccelerometerDataAnalyzer;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.lib.AlertLevel;
import io.github.dronesecurity.lib.AlertType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link AccelerometerDataAnalyzerImpl}.
 */
class AccelerometerAnalyzingTest {

    private static final int CRITICAL_ACCELEROMETER_ANGLE = 50;
    private static final int WARNING_ACCELEROMETER_ANGLE = 35;
    private static final int STABLE_ACCELEROMETER_ANGLE = 10;
    private static final int NONE_INCLINATION = 0;
    private static final String ANGLE_ALERT_TYPE_MESSAGE = "Alert should be of the ANGLE type.";

    /**
     * Tests stable angle.
     */
    @Test
    void accelerometerStableTest() {
        final AccelerometerDataAnalyzer analyzer = new AccelerometerDataAnalyzerImpl();

        final ProcessedAccelerometerData stableData =
                new ProcessedAccelerometerData(STABLE_ACCELEROMETER_ANGLE,
                        NONE_INCLINATION,
                        STABLE_ACCELEROMETER_ANGLE);

        final Alert alert = analyzer.analyzeAccelerometerData(stableData);

        Assertions.assertEquals(AlertType.ANGLE, alert.getAlertType(), ANGLE_ALERT_TYPE_MESSAGE);
        Assertions.assertEquals(AlertLevel.STABLE, alert.getAlertLevel(), "Alert level should be STABLE.");
    }

    /**
     * Tests warning angle.
     */
    @Test
    void accelerometerWarningTest() {
        final AccelerometerDataAnalyzer analyzer = new AccelerometerDataAnalyzerImpl();

        final ProcessedAccelerometerData warningData =
                new ProcessedAccelerometerData(WARNING_ACCELEROMETER_ANGLE,
                        STABLE_ACCELEROMETER_ANGLE,
                        NONE_INCLINATION);

        final Alert alert = analyzer.analyzeAccelerometerData(warningData);

        Assertions.assertEquals(AlertType.ANGLE, alert.getAlertType(), ANGLE_ALERT_TYPE_MESSAGE);
        Assertions.assertEquals(AlertLevel.WARNING, alert.getAlertLevel(), "Alert level should be in WARNING state.");
    }

    /**
     * Tests critical angle.
     */
    @Test
    void accelerometerCriticalTest() {
        final AccelerometerDataAnalyzer analyzer = new AccelerometerDataAnalyzerImpl();

        final ProcessedAccelerometerData criticalData =
                new ProcessedAccelerometerData(WARNING_ACCELEROMETER_ANGLE,
                        CRITICAL_ACCELEROMETER_ANGLE,
                        STABLE_ACCELEROMETER_ANGLE);

        final Alert alert = analyzer.analyzeAccelerometerData(criticalData);

        Assertions.assertEquals(AlertType.ANGLE, alert.getAlertType(), ANGLE_ALERT_TYPE_MESSAGE);
        Assertions.assertEquals(AlertLevel.CRITICAL, alert.getAlertLevel(), "Alert level should be CRITICAL.");
    }

    /**
     * Tests that Yaw angle doesn't influence the alert level.
     */
    @Test
    void accelerometerYawTest() {
        final AccelerometerDataAnalyzer analyzer = new AccelerometerDataAnalyzerImpl();

        final ProcessedAccelerometerData criticalData =
                new ProcessedAccelerometerData(STABLE_ACCELEROMETER_ANGLE,
                        NONE_INCLINATION,
                        CRITICAL_ACCELEROMETER_ANGLE);

        final Alert alert = analyzer.analyzeAccelerometerData(criticalData);

        Assertions.assertEquals(AlertType.ANGLE, alert.getAlertType(), ANGLE_ALERT_TYPE_MESSAGE);
        Assertions.assertEquals(AlertLevel.STABLE, alert.getAlertLevel(), "Yaw should NOT influence alert level.");
    }
}
