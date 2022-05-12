package it.unibo.dronesecurity.userapplication.negligence.entities;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Implementation of {@link OpenNegligenceReport}.
 */
class OpenNegligenceReportImpl extends NegligenceReportWithIDImpl implements OpenNegligenceReport {

    OpenNegligenceReportImpl(final NegligenceReportWithID report) {
        super(report.getId(), report);
    }

    @Contract("_ -> new")
    @Override
    public @NotNull ClosedNegligenceReport close(final Instant closingInstant) {
        return new ClosedNegligenceReportImpl(this, closingInstant);
    }
}
