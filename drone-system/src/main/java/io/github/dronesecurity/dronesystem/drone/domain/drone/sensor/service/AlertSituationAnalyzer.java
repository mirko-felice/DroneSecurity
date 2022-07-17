/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.service;

import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.lib.AlertLevel;

/**
 * Analyzer that processes alerts from all sensors and returns the {@link AlertLevel} of the drone.
 */
public interface AlertSituationAnalyzer {

    /**
     * Processes the alerts to track alert level changes.
     * @param orderData the data of the order currently delivered
     * @param previousProximityAlert previous alert of the proximity sensor
     * @param previousAccelerometerAlert previous alert of the accelerometer
     * @param currentProximityAlert current alert of the proximity sensor
     * @param currentAccelerometerAlert current alert of the accelerometer
     * @return current alert level of the whole drone
     */
    AlertLevel analyzeAlerts(OrderData orderData,
                             Alert previousProximityAlert,
                             Alert previousAccelerometerAlert,
                             Alert currentProximityAlert,
                             Alert currentAccelerometerAlert);
}
