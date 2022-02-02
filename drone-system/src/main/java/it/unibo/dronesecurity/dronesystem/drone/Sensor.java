package it.unibo.dronesecurity.dronesystem.drone;

/**
 * Class representing a generic Sensor that returns a data of R type.
 *
 * @param <R> type of sensor data
 */
public interface Sensor<R> {

    /**
     * Informs if the sensor is active or not.
     *
     * @return The sensor active or not status
     */
    boolean isOn();

    /**
     * Activates this sensor.
     */
    void activate();

    /**
     * Deactivates this sensor.
     */
    void deactivate();

    /**
     * Analyzes last raw data detected and transforms it in readable values.
     */
    void readValue();

    /**
     * Gives the last readable data obtained by the raw.
     *
     * @return Readable values obtained by the last analysis
     */
    R getReadableValue();
}
