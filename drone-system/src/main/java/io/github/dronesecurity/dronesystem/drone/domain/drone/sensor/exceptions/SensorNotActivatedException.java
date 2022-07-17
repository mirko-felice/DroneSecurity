/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.exceptions;

import com.amazonaws.s3.model.InvalidObjectStateException;

/**
 * Exception representing a situation in which an invalid operation is performed on a deactivated sensor.
 */
public class SensorNotActivatedException extends InvalidObjectStateException {
    private static final long serialVersionUID = 0L;

    /**
     * Builds the sensor not activated exception.
     * @param builder The builder of the exception
     */
    protected SensorNotActivatedException(final BuilderImpl builder) {
        super(builder);
    }
}
