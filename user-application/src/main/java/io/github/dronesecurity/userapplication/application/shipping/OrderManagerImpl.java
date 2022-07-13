/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.shipping;

import io.github.dronesecurity.userapplication.domain.shipping.client.Client;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.PlacedOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.exceptions.EstimatedArrivalCannotBeBeforeTodayException;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.Product;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.repo.OrderRepository;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.service.OrderManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Implementation of {@link OrderManager}.
 */
public final class OrderManagerImpl implements OrderManager {

    private final OrderRepository repository;

    /**
     * Build the manager.
     * @param orderRepository {@link OrderRepository} used to persist changes of aggregates
     */
    public OrderManagerImpl(final OrderRepository orderRepository) {
        this.repository = orderRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Order> listOrders() {
        return this.repository.listOrders();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Order retrieveOrderById(final OrderIdentifier identifier) {
        return this.repository.retrieveOrderById(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void placeOrder(final Client who, final Product what, final @NotNull OrderDate when) {
        if (!when.isAfter(OrderDate.TODAY))
            throw new EstimatedArrivalCannotBeBeforeTodayException();
        this.repository.placed(PlacedOrder.place(
                this.repository.nextOrderIdentifier(),
                what,
                who,
                OrderDate.TODAY,
                when));
    }
}
