/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.events;

import io.github.dronesecurity.lib.AlertType;
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
        DomainEvents.register(WarningSituation.class, this::onEventRaised);
        final WarningSituation event = new WarningSituation(EVENT_ALERT_TEST.toString());
        Assertions.assertFalse(this.wasEventRaised);
        DomainEvents.raise(event);
        Assertions.assertTrue(this.wasEventRaised);
    }

    private void onEventRaised(final WarningSituation event) {
        this.wasEventRaised = EVENT_ALERT_TEST == event.getType();
    }
}
