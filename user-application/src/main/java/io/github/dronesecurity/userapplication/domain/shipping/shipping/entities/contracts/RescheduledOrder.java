/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts;

import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;

/**
 * Represents an {@link Order} that has been rescheduled.
 */
public interface RescheduledOrder extends PlacedOrder {

    /**
     * Gets the new {@link OrderDate} date.
     * @return the new date in which the order should arrive
     */
    OrderDate getNewEstimatedArrival();
}
