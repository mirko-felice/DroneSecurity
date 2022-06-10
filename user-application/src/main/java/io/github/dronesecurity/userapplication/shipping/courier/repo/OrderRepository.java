/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.shipping.courier.repo;

import io.github.dronesecurity.userapplication.shipping.courier.entities.*;
import io.vertx.core.Future;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Repository to perform different actions on {@link Order} entity.
 */
public interface OrderRepository {

    /**
     * Get orders.
     * @return list of orders
     */
    Future<List<Order>> getOrders();

    /**
     * Get order using its identifier.
     * @param orderId order identifier
     * @return the order
     */
    Future<Order> getOrderById(String orderId);

    /**
     * Save the delivering order.
     * @param order the order to save
     * @return {@link Future} indicating when the operation is completed
     */
    Future<Void> delivering(DeliveringOrder order);

    /**
     * Save the delivered order.
     * @param order the order to save
     * @return {@link Future} indicating when the operation is completed
     */
    Future<Void> confirmedDelivery(DeliveredOrder order);

    /**
     * Save the failed order.
     * @param order the order to save
     * @return {@link Future} indicating when the operation is completed
     */
    Future<Void> failedDelivery(FailedOrder order);

    /**
     * Save the rescheduled order.
     * @param order the order to save
     * @return {@link Future} indicating when the operation is completed
     */
    Future<Void> rescheduled(RescheduledOrder order);

    /**
     * Get the instance of this repository.
     * @return the instance
     */
    @Contract(value = " -> new", pure = true)
    static @NotNull OrderRepository getInstance() {
        return OrderRepositoryImpl.getInstance();
    }
}
