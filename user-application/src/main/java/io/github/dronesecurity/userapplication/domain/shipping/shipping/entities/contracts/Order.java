/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.dronesecurity.lib.Entity;
import io.github.dronesecurity.userapplication.domain.shipping.client.Client;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.Product;
import io.github.dronesecurity.userapplication.infrastructure.shipping.serializers.OrderDeserializer;
import io.github.dronesecurity.userapplication.infrastructure.shipping.serializers.OrderSerializer;

/**
 * {@link Entity} representing the order.
 */
@JsonDeserialize(using = OrderDeserializer.class)
@JsonSerialize(using = OrderSerializer.class)
public interface Order extends Entity<Order> {

    /**
     * Gets the order identifier.
     * @return the {@link OrderIdentifier}
     */
    OrderIdentifier getId();

    /**
     * Gets the ordered product.
     * @return the {@link Product}
     */
    Product getProduct();

    /**
     * Gets the date in which the order has been placed.
     * @return the {@link OrderDate}
     */
    OrderDate getPlacingDate();

    /**
     * Gets the date in which the order should arrive.
     * @return the {@link OrderDate}
     */
    OrderDate getEstimatedArrival();

    /**
     * Gets the current state of the order.
     * @return the {@link OrderState}
     */
    OrderState getCurrentState();

    /**
     * Gets the client.
     * @return the {@link Client}
     */
    Client getClient();
}
