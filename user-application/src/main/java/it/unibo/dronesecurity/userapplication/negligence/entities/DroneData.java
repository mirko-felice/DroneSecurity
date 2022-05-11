package it.unibo.dronesecurity.userapplication.negligence.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.unibo.dronesecurity.userapplication.negligence.serializers.DroneDataSerializer;

import java.util.Map;

/**
 * Immutable Value Object representing Drone Data.
 */
@JsonSerialize(using = DroneDataSerializer.class)
public interface DroneData {

    /**
     * Gets the proximity data.
     * @return the proximity
     */
    Double getProximity();

    /**
     * Gets the accelerometer data.
     * @return the accelerometer
     */
    Map<String, Double> getAccelerometer();

    /**
     * Copy this object.
     * @return a new fresh copy
     */
     DroneData deepCopy();

    /**
     * Checks if data are empty.
     * @return true if data are empty, false otherwise
     */
    boolean isEmpty();

}
