/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.repo;

import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.*;

import java.util.List;

/**
 * Repository to persist changes on the {@link Order} aggregate.
 */
public interface OrderRepository {

    /**
     * List all orders.
     * @return {@link List} of {@link Order}
     */
    List<Order> listOrders();

    /**
     * Retrieve an {@link Order} using its identifier.
     * @param orderId {@link OrderIdentifier} used to retrieve the order
     * @return the {@link Order}
     */
    Order retrieveOrderById(OrderIdentifier orderId);

    /**
     * Retrieve the next {@link OrderIdentifier}.
     * @return the {@link OrderIdentifier}
     */
    OrderIdentifier nextOrderIdentifier();

    /**
     * Save the placed order.
     * @param order the order to save
     */
    void placed(PlacedOrder order);

    /**
     * Save the delivering order.
     * @param order the order to save
     */
    void delivering(DeliveringOrder order);

    /**
     * Save the succeeded order.
     * @param order the order to save
     */
    void succeeded(SucceededOrder order);

    /**
     * Save the failed order.
     * @param order the order to save
     */
    void failed(FailedOrder order);

    /**
     * Save the rescheduled order.
     * @param order the order to save
     */
    void rescheduled(RescheduledOrder order);

}
