/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.impl;

import io.github.dronesecurity.userapplication.domain.shipping.client.Client;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.OrderState;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.exceptions.ArrivalDateBeforePlacingDateException;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.Product;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract class to construct a generic {@link Order}.
 */
public abstract class AbstractOrder implements Order {

    private final OrderIdentifier id;
    private final Product product;
    private final Client client;
    private final OrderDate placingDate;
    private final OrderDate estimatedArrival;
    private final OrderState state;

    /**
     * Build a generic Order.
     * @param id the {@link OrderIdentifier}
     * @param product the ordered {@link Product}
     * @param client the {@link Client} who placed the order
     * @param placingDate {@link OrderDate} -{@literal >} the date in which the order has been placed
     * @param estimatedArrival {@link OrderDate} -{@literal >} the date in which the order should arrive
     * @param state {@link OrderState} representing current order state
     */
    protected AbstractOrder(final OrderIdentifier id,
                            final Product product,
                            final Client client,
                            final @NotNull OrderDate placingDate,
                            final @NotNull OrderDate estimatedArrival,
                            final OrderState state) {
        if (estimatedArrival.isBefore(placingDate))
            throw new ArrivalDateBeforePlacingDateException();

        this.id = id;
        this.product = product;
        this.client = client;
        this.placingDate = placingDate;
        this.estimatedArrival = estimatedArrival;
        this.state = state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderIdentifier getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product getProduct() {
        return this.product;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Client getClient() {
        return this.client;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderDate getPlacingDate() {
        return this.placingDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderDate getEstimatedArrival() {
        return this.estimatedArrival;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderState getCurrentState() {
        return this.state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSameIdentityAs(final @NotNull Order entity) {
        return this.id.isSameValueAs(entity.getId());
    }
}
