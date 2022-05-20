/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.shipping.courier.entities;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Represents an {@link Order} that is currently only placed.
 */
public class PlacedOrder extends AbstractOrder {

    /**
     * Build the placed Order.
     * @param id the order identifier
     * @param product the ordered product
     * @param placingDate the date in which the order has been placed
     * @param estimatedArrival the date in which the order should arrive
     */
    public PlacedOrder(final String id, final String product, final Instant placingDate,
                       final Instant estimatedArrival) {
        super(id, product, placingDate, estimatedArrival);
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public @NotNull String getCurrentState() {
        return "Order is placed.";
    }

    /**
     * Deliver the Order returning the corresponding entity.
     * @return the {@link Order} that is being delivered.
     */
    @Contract(" -> new")
    public @NotNull DeliveringOrder deliver() {
        return new DeliveringOrder(this.getId(), this.getProduct(), this.getPlacingDate(), this.getEstimatedArrival());
    }
}
