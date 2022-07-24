/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.events;

import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.SucceededOrder;
import io.github.dronesecurity.userapplication.events.DomainEvent;

/**
 * {@link DomainEvent} representing an {@link Order} that has completed successfully the delivery.
 */
public final class OrderSucceeded implements DomainEvent {

    private final SucceededOrder order;

    /**
     * Builds the event.
     * @param order {@link SucceededOrder} just updated
     */
    public OrderSucceeded(final SucceededOrder order) {
        this.order = order;
    }

    /**
     * Gets the order that has completed successfully the delivery.
     * @return the {@link SucceededOrder}
     */
    public SucceededOrder getOrder() {
        return this.order;
    }
}
