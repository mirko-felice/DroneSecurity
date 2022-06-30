/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.events;

import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.RescheduledOrder;
import io.github.dronesecurity.userapplication.events.DomainEvent;

/**
 * {@link DomainEvent} representing an {@link Order} that has been rescheduled.
 */
public class OrderRescheduled implements DomainEvent {

    private final RescheduledOrder order;

    /**
     * Builds the event.
     * @param order {@link RescheduledOrder} just updated
     */
    public OrderRescheduled(final RescheduledOrder order) {
        this.order = order;
    }

    /**
     * Gets the order that has been rescheduled.
     * @return the {@link RescheduledOrder}
     */
    public RescheduledOrder getOrder() {
        return this.order;
    }
}
