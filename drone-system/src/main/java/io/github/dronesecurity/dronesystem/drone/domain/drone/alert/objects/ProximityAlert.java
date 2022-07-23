/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects;

import io.github.dronesecurity.lib.shared.AlertLevel;
import io.github.dronesecurity.lib.shared.AlertType;

/**
 * Alert representing the alert specific to the proximity sensor.
 */
public class ProximityAlert extends Alert {

    private final double distance;

    /**
     * Builds the Alert with its own type and level.
     * @param alertType The type of the alert
     * @param alertLevel The gravity of the alert
     * @param distance The distance detected by the proximity sensor
     */
    public ProximityAlert(final AlertType alertType, final AlertLevel alertLevel, final double distance) {
        super(alertType, alertLevel);
        this.distance = distance;
    }

    /**
     * Gets the distance related to the alert.
     * @return The distance of the alert
     */
    public double getDistance() {
        return this.distance;
    }
}
