/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.events;

import io.github.dronesecurity.lib.AlertType;

/**
 * The event to be raised when the drone informs of a warning situation.
 */
public class DangerousSituation implements DomainEvent {

    private final AlertType type;

    /**
     * Instantiates the Warning event.
     *
     * @param type type of the alert
     */
    public DangerousSituation(final String type) {
        this.type = AlertType.valueOf(type);
    }

    /**
     * Gets the type of the alert.
     *
     * @return the {@link AlertType}
     */
    public AlertType getType() {
        return this.type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.type == AlertType.ANGLE
                ? SituationConstants.DANGEROUS_ANGLE
                : SituationConstants.DANGEROUS_DISTANCE;
    }
}
