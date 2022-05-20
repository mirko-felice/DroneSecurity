/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.events;

import io.github.dronesecurity.lib.AlertType;

/**
 * The event to be raised when the drone informs of a critical situation.
 */
public class CriticalSituation implements Event {
    private final AlertType type;

    /**
     * Instantiates the Critical event.
     *
     * @param type type of the alert
     */
    public CriticalSituation(final String type) {
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
}
