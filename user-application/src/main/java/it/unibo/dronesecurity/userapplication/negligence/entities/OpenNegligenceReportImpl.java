package it.unibo.dronesecurity.userapplication.negligence.entities;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Implementation of {@link OpenNegligenceReport}.
 */
public final class OpenNegligenceReportImpl extends BaseNegligenceReport implements OpenNegligenceReport {

    /**
     * Build the report.
     * @param builder builder containing all information needed
     */
    OpenNegligenceReportImpl(final @NotNull Builder builder) {
        super(builder);
    }

    @Contract("_ -> new")
    @Override
    public @NotNull ClosedNegligenceReport close(final Instant closingInstant) {
        return new ClosedNegligenceReportImpl(this.generateBaseBuilder(), closingInstant);
    }
}
