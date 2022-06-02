/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.exceptions;

import io.github.dronesecurity.userapplication.shipping.courier.entities.Order;

/**
 * Exception thrown if {@link Order} is built assigning an arrival date before placing date.
 */
public final class OrderDateException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Build the exception.
     */
    public OrderDateException() {
        super("Order arrival date MUST NOT be before placing date!");
    }
}
