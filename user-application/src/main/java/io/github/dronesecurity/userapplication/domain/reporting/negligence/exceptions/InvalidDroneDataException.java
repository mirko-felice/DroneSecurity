/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.exceptions;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.DroneData;

/**
 * Exception thrown if {@link DroneData} is built using invalid data.
 */
public final class InvalidDroneDataException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Build the exception.
     */
    public InvalidDroneDataException() {
        super("Drone data MUST NOT have invalid data such as "
                + "proximity negative value, angles not in correct range or negative image size!");
    }
}
