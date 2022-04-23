package it.unibo.dronesecurity.dronesystem.drone.report;

import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

/**
 * Simple POJO class to represent a negligence report.
 */
public final class NegligenceReport {

    private final String negligent;
    private final double proximity;
    private final Map<String, Double> accelerometer;

    /**
     * Build the report.
     * @param negligent the negligent leading the drone
     * @param proximity the proximity data detected by its sensor
     * @param accelerometer the accelerometer data detected by its sensor
     */
    public NegligenceReport(final String negligent, final double proximity, final Map<String, Double> accelerometer) {
        this.negligent = negligent;
        this.proximity = proximity;
        this.accelerometer = Map.copyOf(accelerometer);
    }

    /**
     * Gets the negligent that has to be reported.
     * @return the negligent
     */
    public String getNegligent() {
        return this.negligent;
    }

    /**
     * Gets the proximity data.
     * @return the proximity
     */
    public double getProximity() {
        return this.proximity;
    }

    /**
     * Gets the accelerometer data.
     * @return the accelerometer data
     */
    public @Unmodifiable Map<String, Double> getAccelerometer() {
        return Map.copyOf(this.accelerometer);
    }
}
