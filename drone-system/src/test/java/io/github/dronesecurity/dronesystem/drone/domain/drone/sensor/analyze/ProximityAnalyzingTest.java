/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.analyze;

import io.github.dronesecurity.dronesystem.drone.application.drone.proximity.ProximityDataAnalyzerImpl;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.ProcessedProximityData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.services.ProximityDataAnalyzer;
import io.github.dronesecurity.lib.shared.AlertLevel;
import io.github.dronesecurity.lib.shared.AlertType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ProximityDataAnalyzerImpl}.
 */
class ProximityAnalyzingTest {

    private static final double CRITICAL_DISTANCE = 15;
    private static final double WARNING_DISTANCE = 35;
    private static final double STABLE_DISTANCE = 55;
    private static final String DISTANCE_ALERT_TYPE_MESSAGE = "Alert should be of the DISTANCE type.";

    /**
     * Tests stable distance.
     */
    @Test
    void proximityStableTest() {
        final ProximityDataAnalyzer analyzer = new ProximityDataAnalyzerImpl();

        final ProcessedProximityData stableData = new ProcessedProximityData(STABLE_DISTANCE);

        final Alert alert = analyzer.analyzeProximityData(stableData);

        Assertions.assertEquals(AlertType.DISTANCE, alert.getAlertType(), DISTANCE_ALERT_TYPE_MESSAGE);
        Assertions.assertEquals(AlertLevel.STABLE, alert.getAlertLevel(), "Alert level should be STABLE.");
    }

    /**
     * Tests warning distance.
     */
    @Test
    void proximityWarningTest() {
        final ProximityDataAnalyzer analyzer = new ProximityDataAnalyzerImpl();

        final ProcessedProximityData warningData = new ProcessedProximityData(WARNING_DISTANCE);

        final Alert alert = analyzer.analyzeProximityData(warningData);

        Assertions.assertEquals(AlertType.DISTANCE, alert.getAlertType(), DISTANCE_ALERT_TYPE_MESSAGE);
        Assertions.assertEquals(AlertLevel.WARNING, alert.getAlertLevel(), "Alert level should be in WARNING state.");
    }

    /**
     * Tests critical distance.
     */
    @Test
    void proximityCriticalTest() {
        final ProximityDataAnalyzer analyzer = new ProximityDataAnalyzerImpl();

        final ProcessedProximityData criticalData = new ProcessedProximityData(CRITICAL_DISTANCE);

        final Alert alert = analyzer.analyzeProximityData(criticalData);

        Assertions.assertEquals(AlertType.DISTANCE, alert.getAlertType(), DISTANCE_ALERT_TYPE_MESSAGE);
        Assertions.assertEquals(AlertLevel.CRITICAL, alert.getAlertLevel(), "Alert level should be CRITICAL.");
    }
}
