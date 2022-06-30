/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.exceptions;

import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;

/**
 * Exception thrown if {@link Order} is built assigning an arrival date before placing date.
 */
public final class ArrivalDateBeforePlacingDateException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Build the exception.
     */
    public ArrivalDateBeforePlacingDateException() {
        super("Order arrival date MUST NOT be before placing date!");
    }
}
