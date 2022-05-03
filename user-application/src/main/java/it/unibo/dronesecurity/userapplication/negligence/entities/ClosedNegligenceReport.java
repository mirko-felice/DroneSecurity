package it.unibo.dronesecurity.userapplication.negligence.entities;

import java.time.Instant;

/**
 * Represents CLOSED state of the {@link NegligenceReport}.
 */
public interface ClosedNegligenceReport extends NegligenceReport {

    /**
     * Gets the instant when the report has been closed.
     * @return the {@link Instant}
     */
    Instant getClosingInstant();
}
