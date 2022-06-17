/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.shipping.entities;

import io.github.dronesecurity.userapplication.shipping.utilities.OrderConstants;
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
     * @param client the client who placed the order
     * @param placingDate the date in which the order has been placed
     * @param estimatedArrival the date in which the order should arrive
     */
    public PlacedOrder(final long id, final String product, final String client, final Instant placingDate,
                       final Instant estimatedArrival) {
        super(id, product, client, placingDate, estimatedArrival);
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public @NotNull String getCurrentState() {
        return OrderConstants.PLACED_ORDER_STATE;
    }

    /**
     * Deliver the Order returning the corresponding entity.
     * @return the {@link Order} that is being delivered.
     */
    @Contract(" -> new")
    public @NotNull DeliveringOrder deliver() {
        return new DeliveringOrder(this.getId(), this.getProduct(), this.getClient(), this.getPlacingDate(),
                this.getEstimatedArrival());
    }
}
