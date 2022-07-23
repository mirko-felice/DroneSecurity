/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects;

import io.github.dronesecurity.lib.shared.AlertLevel;
import io.github.dronesecurity.lib.shared.AlertType;

/**
 * Alert data structure that contains {@link AlertType} and {@link AlertLevel}.
 */
public class Alert {

    private final AlertType alertType;
    private final AlertLevel alertLevel;

    /**
     * Builds the Alert with its own type and level.
     * @param alertType The type of the alert
     * @param alertLevel The gravity of the alert
     */
    public Alert(final AlertType alertType, final AlertLevel alertLevel) {
        this.alertType = alertType;
        this.alertLevel = alertLevel;
    }

    /**
     * Gets the type of the alert.
     * @return the type of the alert
     */
    public AlertType getAlertType() {
        return this.alertType;
    }

    /**
     * Gets the level of the alert.
     * @return the level of the alert
     */
    public AlertLevel getAlertLevel() {
        return this.alertLevel;
    }
}
