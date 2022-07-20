/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.drone.usermonitoring.events;

import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.ProximityData;
import io.github.dronesecurity.userapplication.events.DomainEvent;

/**
 * The event to be raised when the drone publishes its proximity sensor data.
 */
public final class ProximityRead implements DomainEvent {

    private final ProximityData proximity;

    /**
     * Instantiates the Proximity data Read event.
     *
     * @param proximity data read by the proximity sensor
     */
    public ProximityRead(final ProximityData proximity) {
        this.proximity = proximity;
    }

    /**
     * Gets proximity sensor data.
     *
     * @return value read by the proximity sensor
     */
    public ProximityData getProximity() {
        return this.proximity;
    }
}
