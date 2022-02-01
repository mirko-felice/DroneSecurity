package it.unibo.dronesecurity.userapplication.events;

/**
 * The event to be raised when the drone informs of a warning situation.
 */
public class WarningEvent implements Event {
    private final String msg;

    /**
     * Instantiates the Warning event.
     *
     * @param msg message that was received
     */
    public WarningEvent(final String msg) {
        this.msg = msg;
    }

    /**
     * Gets the message of the event.
     *
     * @return the message read by the event
     */
    public String getMsg() {
        return this.msg;
    }
}
