/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.events;

import io.github.dronesecurity.lib.AlertType;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.events.DangerousSituation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *  Test for domain events.
 */
final class DomainEventTest {

    private static final AlertType EVENT_ALERT_TEST = AlertType.DISTANCE;
    private boolean wasEventRaised;

    /**
     * Domain event raising test.
     */
    @Test
    void raiseEventTest() {
        DomainEvents.register(DangerousSituation.class, this::onEventRaised);
        final DangerousSituation event = new DangerousSituation(EVENT_ALERT_TEST.toString());
        Assertions.assertFalse(this.wasEventRaised);
        DomainEvents.raise(event);
        Assertions.assertTrue(this.wasEventRaised);
    }

    private void onEventRaised(final DangerousSituation event) {
        this.wasEventRaised = EVENT_ALERT_TEST == event.getAlertType();
    }
}
