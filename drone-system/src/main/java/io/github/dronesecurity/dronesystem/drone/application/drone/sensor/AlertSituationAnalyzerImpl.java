/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.application.drone.sensor;

import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.service.AlertLevelPublisher;
import io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.service.AlertSituationAnalyzer;
import io.github.dronesecurity.lib.AlertLevel;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link AlertSituationAnalyzer} that tracks the changes of the alert level among the sensors.
 */
public class AlertSituationAnalyzerImpl implements AlertSituationAnalyzer {

    private final AlertLevelPublisher alertLevelPublisher;

    /**
     * Initializes the analyzer with its relative publisher.
     */
    public AlertSituationAnalyzerImpl() {
        this.alertLevelPublisher = new AlertLevelPublisherImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AlertLevel analyzeAlerts(final OrderData orderData,
                                     final @NotNull Alert previousProximityAlert,
                                     final @NotNull Alert previousAccelerometerAlert,
                                     final @NotNull Alert currentProximityAlert,
                                     final @NotNull Alert currentAccelerometerAlert) {

        if (previousProximityAlert.getAlertLevel() != currentProximityAlert.getAlertLevel()
                || previousAccelerometerAlert.getAlertLevel() != currentAccelerometerAlert.getAlertLevel()) {
            if (currentProximityAlert.getAlertLevel() == AlertLevel.STABLE
                    && currentAccelerometerAlert.getAlertLevel() == AlertLevel.STABLE)
                this.alertLevelPublisher.publishStableAlertLevel(orderData);
            else {
                if (currentProximityAlert.getAlertLevel().compareTo(currentAccelerometerAlert.getAlertLevel()) >= 0)
                    this.alertLevelPublisher.publishCurrentAlertLevel(orderData, currentProximityAlert);
                else
                    this.alertLevelPublisher.publishCurrentAlertLevel(orderData, currentAccelerometerAlert);
            }
        }

        return currentProximityAlert.getAlertLevel().compareTo(currentAccelerometerAlert.getAlertLevel()) >= 0
                ? currentProximityAlert.getAlertLevel() : currentAccelerometerAlert.getAlertLevel();
    }
}
