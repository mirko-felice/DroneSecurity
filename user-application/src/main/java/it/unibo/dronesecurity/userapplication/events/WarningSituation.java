package it.unibo.dronesecurity.userapplication.events;

import it.unibo.dronesecurity.lib.AlertType;

/**
 * The event to be raised when the drone informs of a warning situation.
 */
public class WarningSituation implements Event {
    private final AlertType type;

    /**
     * Instantiates the Warning event.
     *
     * @param type type of the alert
     */
    public WarningSituation(final String type) {
        this.type = AlertType.valueOf(type);
    }

    /**
     * Gets the type of the alert.
     *
     * @return the {@link AlertType}
     */
    public AlertType getType() {
        return this.type;
    }
}
