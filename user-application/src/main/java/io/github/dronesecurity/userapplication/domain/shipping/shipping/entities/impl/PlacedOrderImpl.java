/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.impl;

import io.github.dronesecurity.userapplication.domain.shipping.client.Client;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.DeliveringOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.OrderState;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.PlacedOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.events.OrderDelivering;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.Product;
import io.github.dronesecurity.userapplication.events.DomainEvents;

/**
 * Implementation of {@link PlacedOrder}.
 */
public final class PlacedOrderImpl extends AbstractOrder implements PlacedOrder {

    /**
     * Build the Placed Order.
     * @param id the {@link OrderIdentifier}
     * @param product the ordered {@link Product}
     * @param client the {@link Client} who placed the order
     * @param placingDate {@link OrderDate} -{@literal >} the date in which the order has been placed
     * @param estimatedArrival {@link OrderDate} -{@literal >} the date in which the order should arrive
     */
    public PlacedOrderImpl(final OrderIdentifier id,
                           final Product product,
                           final Client client,
                           final OrderDate placingDate,
                           final OrderDate estimatedArrival) {
        super(id, product, client, placingDate, estimatedArrival, OrderState.PLACED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startDelivering(final String droneId, final String courierUsername) {
        final DeliveringOrder order = new DeliveringOrderImpl(
                this.getId(),
                this.getProduct(),
                this.getClient(),
                this.getPlacingDate(),
                this.getEstimatedArrival());
        DomainEvents.raise(new OrderDelivering(order, droneId, courierUsername));
    }
}
