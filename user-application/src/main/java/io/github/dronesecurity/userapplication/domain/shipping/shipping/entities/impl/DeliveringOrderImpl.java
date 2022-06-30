/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.impl;

import io.github.dronesecurity.userapplication.domain.shipping.client.Client;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.*;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.events.OrderFailed;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.events.OrderSucceeded;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.Product;
import io.github.dronesecurity.userapplication.events.DomainEvents;

/**
 * Implementation of {@link DeliveringOrder}.
 */
public final class DeliveringOrderImpl extends AbstractOrder implements DeliveringOrder {

    /**
     * Build the Delivering Order.
     * @param id the {@link OrderIdentifier}
     * @param product the ordered {@link Product}
     * @param client the {@link Client} who placed the order
     * @param placingDate {@link OrderDate} -{@literal >} the date in which the order has been placed
     * @param estimatedArrival {@link OrderDate} -{@literal >} the date in which the order should arrive
     */
    public DeliveringOrderImpl(final OrderIdentifier id,
                               final Product product,
                               final Client client,
                               final OrderDate placingDate,
                               final OrderDate estimatedArrival) {
        super(id, product, client, placingDate, estimatedArrival, OrderState.DELIVERING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void succeedDelivery() {
        final SucceededOrder order = new SucceededOrderImpl(
                this.getId(),
                this.getProduct(),
                this.getClient(),
                this.getPlacingDate(),
                this.getEstimatedArrival());
        DomainEvents.raise(new OrderSucceeded(order));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void failDelivery() {
        final FailedOrder order = new FailedOrderImpl(
                this.getId(),
                this.getProduct(),
                this.getClient(),
                this.getPlacingDate(),
                this.getEstimatedArrival());
        DomainEvents.raise(new OrderFailed(order));
    }

}
