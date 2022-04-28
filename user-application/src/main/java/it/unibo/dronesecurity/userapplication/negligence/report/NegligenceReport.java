package it.unibo.dronesecurity.userapplication.negligence.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibo.dronesecurity.lib.MqttMessageParameterConstants;
import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.negligence.serializers.NegligenceReportDeserializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the report used to record the commitment of a negligence by a {@link Courier}.
 */
@JsonDeserialize(using = NegligenceReportDeserializer.class)
public final class NegligenceReport {

    private final Courier negligent;
    private final ObjectNode data;
    private final Maintainer assigner;

    private NegligenceReport(final @NotNull Builder builder) {
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
        this.assigner = builder.assigner;
    }

    /**
     * Gets the negligent as a {@link Courier}.
     * @return the negligent
     */
    public Courier getNegligent() {
        return this.negligent;
    }

    /**
     * Gets the data collected in that instant.
     * @return the {@link ObjectNode} representing the data
     */
    public ObjectNode getData() {
        return this.data.deepCopy();
    }

    /**
     * .
     * @return .
     */
    public Maintainer getAssigner() {
        return this.assigner;
    }

    /**
     * Builder class to apply Builder Pattern in order to differentiate multiple type of instantiation.
     */
    public static final class Builder {

        private final Courier negligent;
        private Double proximity;
        private Map<String, Double> accelerometerData;
        private final Maintainer assigner;

        private Builder(final Courier negligent, final Maintainer assigner) {
            this.negligent = negligent;
            this.assigner = assigner;
            this.accelerometerData = new HashMap<>();
        }

        /**
         * Creates builder starting from the negligent.
         * @param negligent the {@link Courier} that has committed negligence
         * @param assigner the {@link Maintainer} assigned to the report
         * @return a new {@link Builder}
         */
        public static @NotNull Builder fromNegligent(final Courier negligent, final Maintainer assigner) {
            return new Builder(negligent, assigner);
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
        public Builder withAccelerometerData(final Map<String, Double> accelerometerSensorData) {
            this.accelerometerData = Map.copyOf(accelerometerSensorData);
            return this;
        }

        /**
         * Construct the report.
         * @return a new {@link NegligenceReport}
         */
        @Contract(" -> new")
        public @NotNull NegligenceReport build() {
            return new NegligenceReport(this);
        }
    }
}
