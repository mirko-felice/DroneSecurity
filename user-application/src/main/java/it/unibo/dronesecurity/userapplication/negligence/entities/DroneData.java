package it.unibo.dronesecurity.userapplication.negligence.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.dronesecurity.userapplication.negligence.utilities.NegligenceConstants;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Immutable Value Object representing Drone Data.
 */
public class DroneData {

    private final JsonNode copy;
    private Double proximity;
    private final Map<String, Double> accelerometer;

    /**
     * Build an empty Drone Data.
     */
    public DroneData() {
        this(new ObjectMapper().createObjectNode());
    }

    /**
     * Build the Object starting from data.
     * @param data object to retrieve data from
     */
    public DroneData(final @NotNull JsonNode data) {
        this.copy = data.deepCopy();

        if (data.has(NegligenceConstants.PROXIMITY))
            this.proximity = data.get(NegligenceConstants.PROXIMITY).asDouble();
        this.accelerometer = new HashMap<>();
        if (data.has(NegligenceConstants.ACCELEROMETER))
            data.get(NegligenceConstants.ACCELEROMETER).fields().forEachRemaining(entry ->
                this.accelerometer.put(entry.getKey(), entry.getValue().asDouble()));
    }

    /**
     * Gets the proximity data.
     * @return the proximity
     */
    public Double getProximity() {
        return this.proximity;
    }

    /**
     * Gets the accelerometer data.
     * @return the accelerometer
     */
    public Map<String, Double> getAccelerometer() {
        return Map.copyOf(this.accelerometer);
    }

    /**
     * Copy this object.
     * @return a new fresh copy
     */
    public DroneData deepCopy() {
        return new DroneData(this.copy);
    }

    /**
     * Checks if data are empty.
     * @return true if data are empty, false otherwise
     */
    public boolean isEmpty() {
        return this.copy.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Sensor Data (click for details)";
    }
}
