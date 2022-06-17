/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.common.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.dronesecurity.userapplication.common.data.serializers.DroneDataSerializer;
import io.github.dronesecurity.userapplication.common.data.serializers.DroneDataDeserializer;

import java.util.Map;

/**
 * Wrapper class for all the sensors' data detected.
 */
@JsonDeserialize(using = DroneDataDeserializer.class)
@JsonSerialize(using = DroneDataSerializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
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
    Map<String, Integer> getAccelerometer();

    /**
     * Gets the camera data.
     * @return the camera data
     */
    Long getCamera();

    /**
     * Copy this object.
     * @return a new fresh copy
     */
    DroneData deepCopy();
}
