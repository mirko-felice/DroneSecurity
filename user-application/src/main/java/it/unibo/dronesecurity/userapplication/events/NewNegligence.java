package it.unibo.dronesecurity.userapplication.events;

import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.negligence.entities.NegligenceReport;
import org.jetbrains.annotations.NotNull;

/**
 * The event to be raised when a {@link Courier} commits negligence.
 */
public class NewNegligence implements Event {

    private final NegligenceReport report;

    /**
     * Build the event using the {@link NegligenceReport}.
     * @param report report used to build the event
     */
    public NewNegligence(final @NotNull NegligenceReport report) {
        this.report = report;
    }

    /**
     * Gets the {@link NegligenceReport} representing the negligence committed by a {@link Courier}.
     * @return the report
     */
    public NegligenceReport getReport() {
        return this.report;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "NewNegligence{ report = " + this.report + '}';
    }
}
