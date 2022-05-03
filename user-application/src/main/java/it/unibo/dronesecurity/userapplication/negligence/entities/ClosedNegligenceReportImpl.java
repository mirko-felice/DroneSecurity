package it.unibo.dronesecurity.userapplication.negligence.entities;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Implementation of {@link ClosedNegligenceReport}.
 */
public class ClosedNegligenceReportImpl extends BaseNegligenceReport implements ClosedNegligenceReport {

    private final Instant closingInstant;

    /**
     * Build the report.
     * @param builder builder containing all base information
     * @param closingInstant the instant when the report has been closed
     */
    public ClosedNegligenceReportImpl(final @NotNull Builder builder, final Instant closingInstant) {
        super(builder);
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
