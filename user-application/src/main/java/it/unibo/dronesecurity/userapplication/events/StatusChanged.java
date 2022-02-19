package it.unibo.dronesecurity.userapplication.events;

/**
 * The event to be raised when the drone informs of its status changing.
 */
public class StatusChanged implements Event {
    private final String status;

    /**
     * Instantiates the Status Changed event.
     *
     * @param status status of the drone that was received
     */
    public StatusChanged(final String status) {
        this.status = status;
    }

    /**
     * Gets the status received.
     *
     * @return the status of the drone received
     */
    public String getStatus() {
        return this.status;
    }
}
