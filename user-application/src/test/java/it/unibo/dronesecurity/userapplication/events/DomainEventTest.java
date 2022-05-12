package it.unibo.dronesecurity.userapplication.events;

import it.unibo.dronesecurity.lib.AlertType;
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
        final DomainEvents<WarningSituation> domainEvents = new DomainEvents<>();
        domainEvents.register(this::onEventRaised);
        final WarningSituation event = new WarningSituation(EVENT_ALERT_TEST.toString());
        Assertions.assertFalse(this.wasEventRaised);
        domainEvents.raise(event);
        Assertions.assertTrue(this.wasEventRaised);
    }

    private void onEventRaised(final WarningSituation event) {
        this.wasEventRaised = EVENT_ALERT_TEST == event.getType();
    }
}
