package it.unibo.dronesecurity.userapplication.events;

import java.util.Map;

/**
 * The event to be raised when the drone publishes its sensor data.
 */
public class DataReadEvent implements Event {
    private final double proximity;
    private final Map<String, Double> accelerometerData;
    private final double cameraData;

    /**
     * Instantiates the Data Read event.
     *
     * @param proximity data read by the proximity sensor
     * @param accelerometer data read by the accelerometer
     * @param camera data read by the camera
     */
    public DataReadEvent(final double proximity, final Map<String, Double> accelerometer, final double camera) {
        this.proximity = proximity;
        this.accelerometerData = accelerometer;
        this.cameraData = camera;
    }

    /**
     * Gets proximity sensor data.
     *
     * @return value read bt the proximity sensor
     */
    public double getProximity() {
        return proximity;
    }

    /**
     * Gets accelerometer data.
     *
     * @return value read bt the accelerometer
     */
    public Map<String, Double> getAccelerometerData() {
        return accelerometerData;
    }

    /**
     * Gets camera data.
     *
     * @return value read bt the camera
     */
    public double getCameraData() {
        return cameraData;
    }
}
