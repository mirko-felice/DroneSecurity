/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.monitoring.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.dronesecurity.userapplication.monitoring.serializers.DroneDataDeserializer;
import io.github.dronesecurity.userapplication.monitoring.serializers.DroneDataSerializer;

import java.time.Instant;
import java.util.Map;

/**
 * Immutable Value Object representing Drone Data.
 */
@JsonSerialize(using = DroneDataSerializer.class)
@JsonDeserialize(using = DroneDataDeserializer.class)
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
     * Gets the instant when detection is happened.
     * @return the {@link Instant}
     */
    Instant getDetectionInstant();

    /**
     * Gets the order identifier related to this data detection.
     * @return the order identifier
     */
    long getOrderId();

}
