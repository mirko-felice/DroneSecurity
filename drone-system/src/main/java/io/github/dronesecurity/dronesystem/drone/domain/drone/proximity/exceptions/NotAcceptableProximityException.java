/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.exceptions;

/**
 * Exception to be used when an invalid proximity value is passed as argument.
 */
public class NotAcceptableProximityException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Builds the exception.
     */
    public NotAcceptableProximityException() {
        super("Proximity can NOT be negative.");
    }
}
