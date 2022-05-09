package it.unibo.dronesecurity.userapplication.negligence.entities;

import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.exceptions.ReportEmptyDataException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Base implementation of {@link NegligenceReport}.
 */
public class BaseNegligenceReport implements NegligenceReport {

    private final String negligent;
    private final String assignee;
    private final DroneData data;

    /**
     * Build the report.
     * @param builder builder containing all information needed
     */
    protected BaseNegligenceReport(final @NotNull Builder builder) {
        this.negligent = builder.negligent;
        this.assignee = builder.assignee;
        this.data = builder.data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNegligent() {
        return this.negligent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DroneData getData() {
        return this.data.deepCopy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String assignedTo() {
        return this.assignee;
    }

    /**
     * Generates a Builder from current information.
     * @return a new {@link Builder}
     */
    protected Builder generateBaseBuilder() {
        return new Builder(this.negligent, this.assignee, this.data);
    }

    /**
     * Builder class to apply Builder Pattern in order to differentiate multiple type of instantiation.
     */
    public static final class Builder {

        private final String negligent;
        private final DroneData data;
        private final String assignee;
        private Instant closingInstant;

        /**
         * Creates the builder with needed parameters.
         * @param negligent the {@link Courier} that has committed negligence
         * @param assignee the {@link Maintainer} assigned to the report
         * @param data the {@link DroneData} associated to the report
         */
        public Builder(final String negligent, final String assignee, final DroneData data) {
            this.negligent = negligent;
            this.assignee = assignee;
            this.data = data;
        }

        /**
         * Sets the report as closed.
         * @param instant instant when the report has been closed
         * @return this
         */
        public Builder closed(final Instant instant) {
            if (this.data.isEmpty())
                throw new ReportEmptyDataException();
            this.closingInstant = instant;
            return this;
        }

        /**
         * Construct the report.
         * @return a new {@link NegligenceReport}
         */
        @Contract(" -> new")
        public @NotNull NegligenceReport build() {
            return this.closingInstant == null
                    ? new OpenNegligenceReportImpl(this)
                    : new ClosedNegligenceReportImpl(this, this.closingInstant);
        }
    }
}
