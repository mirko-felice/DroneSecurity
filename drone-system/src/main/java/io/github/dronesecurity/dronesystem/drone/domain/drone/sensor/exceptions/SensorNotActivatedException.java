/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.exceptions;

/**
 * Exception representing a situation in which an invalid operation is performed on a deactivated sensor.
 */
public class SensorNotActivatedException extends IllegalStateException {

    private static final long serialVersionUID = 1L;

    /**
     * Builds the sensor not activated exception.
     */
    public SensorNotActivatedException() {
        super("Sensors cannot publish without being activated first.");
    }
}
