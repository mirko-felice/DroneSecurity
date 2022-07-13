/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.shipping;

import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.DeliveringOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.FailedOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.PlacedOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.events.OrderDelivering;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.events.OrderFailed;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.events.OrderRescheduled;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.events.OrderSucceeded;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.repo.OrderRepository;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.service.DeliveryService;
import io.github.dronesecurity.userapplication.events.DomainEvents;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Implementation of {@link DeliveryService}.
 */
public final class DeliveryServiceImpl implements DeliveryService {

    private final OrderRepository repository;
    private final Consumer<OrderDelivering> orderDeliveringHandler;
    private final Consumer<OrderSucceeded> orderSucceededHandler;
    private final Consumer<OrderFailed> orderFailedHandler;
    private final Consumer<OrderRescheduled> orderRescheduledHandler;

    /**
     * Build the service.
     * @param orderRepository {@link OrderRepository} used to persist changes of aggregates
     */
    public DeliveryServiceImpl(final OrderRepository orderRepository) {
        this.repository = orderRepository;
        this.orderDeliveringHandler = this::onOrderDelivering;
        this.orderSucceededHandler = this::onOrderSucceeded;
        this.orderFailedHandler = this::onOrderFailed;
        this.orderRescheduledHandler = this::onOrderRescheduled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performDelivery(final @NotNull PlacedOrder order, final String droneId, final String courierUsername) {
        DomainEvents.register(OrderDelivering.class, this.orderDeliveringHandler);
        order.startDelivering(droneId, courierUsername);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void succeedDelivery(final @NotNull DeliveringOrder order) {
        DomainEvents.register(OrderSucceeded.class, this.orderSucceededHandler);
        order.succeedDelivery();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void failDelivery(final @NotNull DeliveringOrder order) {
        DomainEvents.register(OrderFailed.class, this.orderFailedHandler);
        order.failDelivery();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rescheduleDelivery(final @NotNull FailedOrder order, final OrderDate newEstimatedArrival) {
        DomainEvents.register(OrderRescheduled.class, this.orderRescheduledHandler);
        order.rescheduleDelivery(newEstimatedArrival);
    }

    private void onOrderDelivering(final @NotNull OrderDelivering orderDelivering) {
        this.repository.delivering(orderDelivering.getOrder());
        DomainEvents.unregister(OrderDelivering.class, this.orderDeliveringHandler);
    }

    private void onOrderSucceeded(final @NotNull OrderSucceeded orderSucceeded) {
        this.repository.succeeded(orderSucceeded.getOrder());
        DomainEvents.unregister(OrderSucceeded.class, this.orderSucceededHandler);
    }

    private void onOrderFailed(final @NotNull OrderFailed orderfailed) {
        this.repository.failed(orderfailed.getOrder());
        DomainEvents.unregister(OrderFailed.class, this.orderFailedHandler);
    }

    private void onOrderRescheduled(final @NotNull OrderRescheduled orderRescheduled) {
        this.repository.rescheduled(orderRescheduled.getOrder());
        DomainEvents.unregister(OrderRescheduled.class, this.orderRescheduledHandler);
    }
}
