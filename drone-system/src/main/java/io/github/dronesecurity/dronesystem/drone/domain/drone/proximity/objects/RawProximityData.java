/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects;

import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.exceptions.NotAcceptableProximityException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Class representing raw proximity sensor data, represented as the distance detected by the sensor.
 */
public class RawProximityData {

    private final double distance;

    /**
     * Builds the raw proximity sensor data.
     *
     * @param distance The distance detected by the proximity sensor
     */
    public RawProximityData(final double distance) {
        if (distance < 0) throw new NotAcceptableProximityException();
        this.distance = BigDecimal.valueOf(distance).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    /**
     * Gets the distance stored.
     * @return The distance detected by the proximity sensor
     */
    public double getDistance() {
        return this.distance;
    }
}
