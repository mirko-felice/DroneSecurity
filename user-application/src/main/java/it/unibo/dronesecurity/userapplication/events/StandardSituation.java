package it.unibo.dronesecurity.userapplication.events;

/**
 * The event to be raised when alert is no more occurring and standard situation comes back.
 */
public class StandardSituation implements Event {

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Alert is no more occurring, situation comes back to a standard form.";
    }
}
