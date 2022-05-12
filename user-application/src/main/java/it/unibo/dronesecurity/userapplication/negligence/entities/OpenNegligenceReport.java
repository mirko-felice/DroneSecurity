package it.unibo.dronesecurity.userapplication.negligence.entities;

import it.unibo.dronesecurity.userapplication.exceptions.ReportEmptyDataException;

import java.time.Instant;

/**
 * Represents OPEN state of the {@link NegligenceReportWithID}.
 */
public interface OpenNegligenceReport extends NegligenceReportWithID {

    /**
     * Close this report.
     * @param closingInstant instant when the report has been closed
     * @return the closed report as a {@link ClosedNegligenceReport}
     * @throws ReportEmptyDataException if drone data of this report are empty
     */
    ClosedNegligenceReport close(Instant closingInstant);
}
