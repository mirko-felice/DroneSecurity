/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.shipping.courier.entities;

import io.github.dronesecurity.userapplication.shipping.courier.utilities.OrderConstants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Represents an {@link Order} that failed to being delivered.
 */
public final class FailedOrder extends AbstractOrder {

    /**
     * Build the failed Order.
     * @param id the order identifier
     * @param product the ordered product
     * @param client the client who placed the order
     * @param placingDate the date in which the order has been placed
     * @param estimatedArrival the date in which the order should have arrived
     */
    public FailedOrder(final long id, final String product, final String client, final Instant placingDate,
                       final Instant estimatedArrival) {
        super(id, product, client, placingDate, estimatedArrival);
    }

    /**
     * Reschedule the Order to be delivered the given @Date.
     * @param newEstimatedArrival the new date in which the order should arrive
     * @return the {@link Order} that has been rescheduled
     */
    @Contract("_ -> new")
    public @NotNull RescheduledOrder rescheduleDelivery(final Instant newEstimatedArrival) {
        return new RescheduledOrder(this.getId(), this.getProduct(), this.getClient(), this.getPlacingDate(),
                this.getEstimatedArrival(), newEstimatedArrival);
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public @NotNull String getCurrentState() {
        return OrderConstants.FAILED_ORDER_STATE;
    }
}
