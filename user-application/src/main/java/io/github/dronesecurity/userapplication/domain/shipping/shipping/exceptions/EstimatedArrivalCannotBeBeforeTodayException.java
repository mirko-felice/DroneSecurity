/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.exceptions;

import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;

/**
 * Exception thrown if {@link OrderDate} during placing order is a date before today.
 */
public final class EstimatedArrivalCannotBeBeforeTodayException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Build the exception.
     */
    public EstimatedArrivalCannotBeBeforeTodayException() {
        super("Order estimated arrival date MUST NOT be before today!");
    }
}
