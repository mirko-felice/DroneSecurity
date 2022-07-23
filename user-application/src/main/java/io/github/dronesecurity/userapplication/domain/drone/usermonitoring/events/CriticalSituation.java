/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.drone.usermonitoring.events;

import io.github.dronesecurity.lib.shared.AlertType;
import io.github.dronesecurity.userapplication.events.DomainEvent;
import io.github.dronesecurity.userapplication.events.SituationConstants;

/**
 * The event to be raised when the drone informs of a critical situation.
 */
public final class CriticalSituation implements DomainEvent {

    private final AlertType alertType;

    /**
     * Instantiates the Critical event.
     *
     * @param type type of the alert
     */
    public CriticalSituation(final String type) {
        this.alertType = AlertType.valueOf(type);
    }

    /**
     * Gets the type of the alert.
     *
     * @return the {@link AlertType}
     */
    public AlertType getAlertType() {
        return this.alertType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.alertType == AlertType.ANGLE
                ? SituationConstants.CRITICAL_ANGLE
                : SituationConstants.CRITICAL_DISTANCE;
    }
}
