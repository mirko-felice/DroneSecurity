/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.impl;

import io.github.dronesecurity.userapplication.domain.shipping.client.Client;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.DeliveringOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.OrderState;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.RescheduledOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.events.OrderDelivering;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.exceptions.RescheduledDateBeforeFirstArrivalDateException;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.Product;
import io.github.dronesecurity.userapplication.events.DomainEvents;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link RescheduledOrder}.
 */
public final class RescheduledOrderImpl extends AbstractOrder implements RescheduledOrder {

    private final OrderDate newEstimatedArrival;

    /**
     * Build the Rescheduled Order.
     * @param id the {@link OrderIdentifier}
     * @param product the ordered {@link Product}
     * @param client the {@link Client} who placed the order
     * @param placingDate {@link OrderDate} -{@literal >} the date in which the order has been placed
     * @param oldArrival {@link OrderDate} -{@literal >} the date in which the order should have been delivered
     * @param newEstimatedArrival {@link OrderDate} -{@literal >} the new date in which the order should arrive
     * @throws RescheduledDateBeforeFirstArrivalDateException if {@code newEstimatedArrival} is before
     * {@code oldArrival}
     */
    public RescheduledOrderImpl(final OrderIdentifier id,
                                final Product product,
                                final Client client,
                                final OrderDate placingDate,
                                final OrderDate oldArrival,
                                final @NotNull OrderDate newEstimatedArrival) {
        super(id, product, client, placingDate, oldArrival, OrderState.RESCHEDULED);
        if (newEstimatedArrival.isBefore(oldArrival))
            throw new RescheduledDateBeforeFirstArrivalDateException();
        this.newEstimatedArrival = newEstimatedArrival;
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
                this.getNewEstimatedArrival());
        DomainEvents.raise(new OrderDelivering(order, droneId, courierUsername));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderDate getNewEstimatedArrival() {
        return this.newEstimatedArrival;
    }

}
