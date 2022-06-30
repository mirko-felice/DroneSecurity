/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.events;

/**
 * The event to be raised when the drone informs of its status changing.
 */
public class StatusChanged implements DomainEvent {
    private final String status;

    /**
     * Instantiates the Status Changed event.
     *
     * @param status status of the drone that was received
     */
    public StatusChanged(final String status) {
        this.status = status;
    }

    /**
     * Gets the status received.
     *
     * @return the status of the drone received
     */
    public String getStatus() {
        return this.status;
    }
}
