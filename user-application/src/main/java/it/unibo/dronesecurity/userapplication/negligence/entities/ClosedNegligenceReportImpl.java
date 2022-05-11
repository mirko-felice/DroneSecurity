package it.unibo.dronesecurity.userapplication.negligence.entities;

import it.unibo.dronesecurity.userapplication.exceptions.ReportEmptyDataException;

import java.time.Instant;

/**
 * Implementation of {@link ClosedNegligenceReport}.
 */
class ClosedNegligenceReportImpl extends NegligenceReportWithIDImpl implements ClosedNegligenceReport {

    private final Instant closingInstant;

    ClosedNegligenceReportImpl(final NegligenceReportWithID report, final Instant closingInstant) {
        super(report.getId(), report);
        if (report.getData().isEmpty())
            throw new ReportEmptyDataException();
        this.closingInstant = closingInstant;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getClosingInstant() {
        return this.closingInstant;
    }
}
