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
 * Represents an {@link Order} that is currently being delivered.
 */
public final class DeliveringOrder extends AbstractOrder {

    /**
     * Build the delivering Order.
     * @param id the order identifier
     * @param product the ordered product
     * @param client the client who placed the order
     * @param placingDate the date in which the order has been placed
     * @param estimatedArrival the date in which the order should arrive
     */
    public DeliveringOrder(final String id, final String product, final String client, final Instant placingDate,
                           final Instant estimatedArrival) {
        super(id, product, client, placingDate, estimatedArrival);
    }

    /**
     * Confirm the delivery.
     * @return the {@link Order} successfully delivered.
     */
    @Contract(" -> new")
    public @NotNull DeliveredOrder confirmDelivery() {
        return new DeliveredOrder(this.getId(), this.getProduct(), this.getClient(), this.getPlacingDate(),
                this.getEstimatedArrival());
    }

    /**
     * Miss the delivery.
     * @return the {@link Order} representing the delivery fail.
     */
    @Contract(" -> new")
    public @NotNull FailedOrder failDelivery() {
        return new FailedOrder(this.getId(), this.getProduct(), this.getClient(), this.getPlacingDate(),
                this.getEstimatedArrival());
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public @NotNull String getCurrentState() {
        return OrderConstants.DELIVERING_ORDER_STATE;
    }
}
