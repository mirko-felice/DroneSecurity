/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.exceptions;

import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.RescheduledOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;

/**
 * Exception thrown if {@link RescheduledOrder} is built using a {@link OrderDate} for new estimated arrival
 * before the old estimated arrival.
 */
public final class RescheduledDateBeforeFirstArrivalDateException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Build the exception.
     */
    public RescheduledDateBeforeFirstArrivalDateException() {
        super("Order rescheduled date MUST NOT be before first arrival date!");
    }
}
