package it.unibo.dronesecurity.userapplication.negligence.entities;

import java.time.Instant;

/**
 * Represents OPEN state of the {@link NegligenceReport}.
 */
public interface OpenNegligenceReport extends NegligenceReport {

    /**
     * Close this report.
     * @param closingInstant instant when the report has been closed
     * @return the closed report as a {@link ClosedNegligenceReport}
     */
    ClosedNegligenceReport close(Instant closingInstant);
}
