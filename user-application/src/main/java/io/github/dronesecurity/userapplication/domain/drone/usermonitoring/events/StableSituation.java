/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.drone.usermonitoring.events;

import io.github.dronesecurity.userapplication.events.DomainEvent;
import io.github.dronesecurity.userapplication.utilities.drone.usermonitoring.SituationConstants;

/**
 * The event to be raised when alert is no more occurring and standard situation comes back.
 */
public final class StableSituation implements DomainEvent {

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return SituationConstants.STABLE;
    }
}
