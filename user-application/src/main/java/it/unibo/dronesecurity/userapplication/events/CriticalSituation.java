package it.unibo.dronesecurity.userapplication.events;

/**
 * The event to be raised when the drone informs of a critical situation.
 */
public class CriticalSituation implements Event {
    private final String message;

    /**
     * Instantiates the Critical event.
     *
     * @param message message that was received
     */
    public CriticalSituation(final String message) {
        this.message = message;
    }

    /**
     * Gets the message of the event.
     *
     * @return the message read by the event
     */
    public String getMessage() {
        return this.message;
    }
}
