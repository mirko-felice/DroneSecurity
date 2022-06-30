/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.events;

import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.FailedOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.events.DomainEvent;

/**
 * {@link DomainEvent} representing an {@link Order} that has failed the delivery.
 */
public class OrderFailed implements DomainEvent {

    private final FailedOrder order;

    /**
     * Builds the event.
     * @param order {@link FailedOrder} just updated
     */
    public OrderFailed(final FailedOrder order) {
        this.order = order;
    }

    /**
     * Gets the order that is has failed the delivery.
     * @return the {@link FailedOrder}
     */
    public FailedOrder getOrder() {
        return this.order;
    }
}
