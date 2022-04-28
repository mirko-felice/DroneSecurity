package it.unibo.dronesecurity.userapplication.events;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *  Test for domain events.
 */
final class DomainEventTest {

    private static final String EVENT_TEST_MESSAGE = "warning test";
    private boolean wasEventRaised;

    /**
     * Domain event raising test.
     */
    @Test
    void raiseEventTest() {
        final DomainEvents<WarningSituation> domainEvents = new DomainEvents<>();
        domainEvents.register(this::onEventRaised);
        final WarningSituation event = new WarningSituation(EVENT_TEST_MESSAGE);
        Assertions.assertFalse(this.wasEventRaised);
        domainEvents.raise(event);
        Assertions.assertTrue(this.wasEventRaised);
    }

    private void onEventRaised(final WarningSituation event) {
        this.wasEventRaised = EVENT_TEST_MESSAGE.equals(event.getMessage());
    }
}
