package it.unibo.dronesecurity.userapplication.negligence.entities;

import java.time.Instant;

/**
 * Represents CLOSED state of the {@link NegligenceReportWithID}.
 */
public interface ClosedNegligenceReport extends NegligenceReportWithID {

    /**
     * Gets the instant when the report has been closed.
     * @return the {@link Instant}
     */
    Instant getClosingInstant();
}
