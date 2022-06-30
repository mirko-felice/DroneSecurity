/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.events;

import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.DeliveringOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.events.DomainEvent;

/**
 * {@link DomainEvent} representing an {@link Order} that is being delivered.
 */
public class OrderDelivering implements DomainEvent {

    private final DeliveringOrder order;
    private final String droneId;

    /**
     * Builds the event.
     * @param order {@link DeliveringOrder} just updated
     * @param droneId drone identifier performing delivery
     */
    public OrderDelivering(final DeliveringOrder order, final String droneId) {
        this.order = order;
        this.droneId = droneId;
    }

    /**
     * Gets the order that is being delivered.
     * @return the {@link DeliveringOrder}
     */
    public DeliveringOrder getOrder() {
        return this.order;
    }

    /**
     * Gets the drone identifier performing delivery.
     * @return the drone identifier
     */
    public String getDroneId() {
        return this.droneId;
    }
}
