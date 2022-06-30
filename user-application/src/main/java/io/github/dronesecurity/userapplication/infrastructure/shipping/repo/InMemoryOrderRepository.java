/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.shipping.repo;

import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.*;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.repo.OrderRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of non-persistent {@link OrderRepository} useful to test business logic.
 */
public final class InMemoryOrderRepository implements OrderRepository {

    private final List<Order> orders = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Order> listOrders() {
        return List.copyOf(this.orders);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Order retrieveOrderById(final OrderIdentifier orderId) {
        return this.orders.stream().filter(order -> order.getId().isSameValueAs(orderId)).findFirst().orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderIdentifier nextOrderIdentifier() {
        return OrderIdentifier.fromLong(this.orders.size() + 1L);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void placed(final PlacedOrder order) {
        this.orders.add(order);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delivering(final DeliveringOrder order) {
        this.replace(order);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void succeeded(final SucceededOrder order) {
        this.replace(order);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void failed(final FailedOrder order) {
        this.replace(order);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rescheduled(final RescheduledOrder order) {
        this.replace(order);
    }

    private void replace(final Order order) {
        this.orders.replaceAll(o -> o.hasSameIdentityAs(order) ? order : o);
    }
}
