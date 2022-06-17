/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.shipping.entities;

import io.github.dronesecurity.userapplication.shipping.utilities.OrderConstants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents an {@link Order} that has been rescheduled.
 */
public final class RescheduledOrder extends PlacedOrder {

    private final Instant newEstimatedArrival;

    /**
     * Build the rescheduled Order.
     * @param id the order identifier
     * @param product the ordered product
     * @param client the client who placed the order
     * @param placingDate the date in which the order has been placed
     * @param oldArrival the date in which the order should have arrived
     * @param newEstimatedArrival the new date in which the order should arrive
     */
    public RescheduledOrder(final long id, final String product, final String client, final Instant placingDate,
                            final Instant oldArrival, final Instant newEstimatedArrival) {
        super(id, product, client, placingDate, oldArrival);
        this.newEstimatedArrival = newEstimatedArrival;
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public @NotNull String getCurrentState() {
        return OrderConstants.RESCHEDULED_ORDER_STATE;
    }

    /**
     * Returns the new estimated arrival date.
     * @return the date in which the order should arrive
     */
    public Instant getNewEstimatedArrival() {
        return this.newEstimatedArrival;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final RescheduledOrder that = (RescheduledOrder) o;
        return this.getNewEstimatedArrival().equals(that.getNewEstimatedArrival());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.getNewEstimatedArrival());
    }
}
