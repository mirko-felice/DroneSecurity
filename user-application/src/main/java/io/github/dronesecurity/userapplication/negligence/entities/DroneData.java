/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.negligence.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.dronesecurity.userapplication.negligence.serializers.DroneDataSerializer;

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
