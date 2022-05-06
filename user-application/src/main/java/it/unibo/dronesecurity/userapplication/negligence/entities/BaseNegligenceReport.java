package it.unibo.dronesecurity.userapplication.negligence.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibo.dronesecurity.lib.MqttMessageParameterConstants;
import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.exceptions.ReportEmptyDataException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Base implementation of {@link NegligenceReport}.
 */
public class BaseNegligenceReport implements NegligenceReport {

    private final String negligent;
    private final ObjectNode data;
    private final String assignee;

    /**
     * Build the report.
     * @param builder builder containing all information needed
     */
    protected BaseNegligenceReport(final @NotNull Builder builder) {
        this.negligent = builder.negligent;
        final ObjectMapper mapper = new ObjectMapper();
        this.data = mapper.createObjectNode();
        if (builder.proximity != null)
            this.data.put(MqttMessageParameterConstants.PROXIMITY_PARAMETER, builder.proximity);
        if (!builder.accelerometerData.isEmpty()) {
            final ObjectNode accelerometerValues = mapper.createObjectNode();
            builder.accelerometerData.forEach(accelerometerValues::put);
            this.data.set(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER, accelerometerValues);
        }
        this.assignee = builder.assignee;
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
    public ObjectNode getData() {
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
        return new Builder(this.negligent, this.assignee)
                .withProximity(this.data.get(MqttMessageParameterConstants.PROXIMITY_PARAMETER).asDouble())
                .withAccelerometerData(this.data.get(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER));
    }

    /**
     * Builder class to apply Builder Pattern in order to differentiate multiple type of instantiation.
     */
    public static final class Builder {

        private final String negligent;
        private Double proximity;
        private final Map<String, Double> accelerometerData;
        private final String assignee;
        private Instant closingInstant;

        /**
         * Creates the builder with needed parameters.
         * @param negligent the {@link Courier} that has committed negligence
         * @param assignee the {@link Maintainer} assigned to the report
         */
        public Builder(final String negligent, final String assignee) {
            this.negligent = negligent;
            this.assignee = assignee;
            this.accelerometerData = new HashMap<>();
        }

        /**
         * Add proximity value to the report.
         * @param proximitySensorData proximity data
         * @return this
         */
        public Builder withProximity(final Double proximitySensorData) {
            this.proximity = proximitySensorData;
            return this;
        }

        /**
         * Add accelerometer values to the report.
         * @param accelerometerSensorData accelerometer data
         * @return this
         */
        @Contract("_ -> this")
        public Builder withAccelerometerData(final @NotNull JsonNode accelerometerSensorData) {
            this.accelerometerData.put(
                    MqttMessageParameterConstants.ACCELEROMETER_X_PARAMETER,
                    accelerometerSensorData.get(MqttMessageParameterConstants.ACCELEROMETER_X_PARAMETER).asDouble());
            this.accelerometerData.put(
                    MqttMessageParameterConstants.ACCELEROMETER_Y_PARAMETER,
                    accelerometerSensorData.get(MqttMessageParameterConstants.ACCELEROMETER_Y_PARAMETER).asDouble());
            this.accelerometerData.put(
                    MqttMessageParameterConstants.ACCELEROMETER_Z_PARAMETER,
                    accelerometerSensorData.get(MqttMessageParameterConstants.ACCELEROMETER_Z_PARAMETER).asDouble());
            return this;
        }

        /**
         * Sets the report as closed.
         * @param instant instant when the report has been closed
         * @return this
         */
        public Builder closed(final Instant instant) {
            if (this.proximity == null && this.accelerometerData.isEmpty())
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
