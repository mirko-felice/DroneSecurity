/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.shipping.courier.entities;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Represents an {@link Order} that has been successfully delivered.
 */
public final class DeliveredOrder extends AbstractOrder {

    /**
     * Build the delivered Order.
     * @param id the order identifier
     * @param product the ordered product
     * @param placingDate the date in which the order has been placed
     * @param arrival the date in which the order arrived
     */
    public DeliveredOrder(final String id, final String product, final Instant placingDate, final Instant arrival) {
        super(id, product, placingDate, arrival);
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public @NotNull String getCurrentState() {
        return "Order is delivered.";
    }
}