package it.unibo.dronesecurity.userapplication.events;

/**
 * The event to be raised when the drone informs of a warning situation.
 */
public class WarningSituation implements Event {
    private final String message;

    /**
     * Instantiates the Warning event.
     *
     * @param message message that was received
     */
    public WarningSituation(final String message) {
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
