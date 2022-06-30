/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.service;

import io.github.dronesecurity.userapplication.domain.shipping.client.Client;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.Product;

import java.util.List;

/**
 * Domain Service dedicated to manage the orders.
 */
public interface OrderManager {

    /**
     * List all the orders.
     * @return {@link List} of {@link Order}
     */
    List<Order> listOrders();

    /**
     * Retrieve an {@link Order} using its {@link OrderIdentifier}.
     * @param identifier {@link OrderIdentifier} to identify the order
     * @return the {@link Order}
     */
    Order retrieveOrderById(OrderIdentifier identifier);

    /**
     * Place an order using needed information.
     * @param who {@link Client} placing the order
     * @param what {@link Product} ordered
     * @param when {@link OrderDate} estimated arrival
     */
    void placeOrder(Client who, Product what, OrderDate when);
}
