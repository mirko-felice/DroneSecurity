/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts;

import io.github.dronesecurity.userapplication.domain.shipping.client.Client;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.impl.PlacedOrderImpl;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.Product;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an {@link Order} that is currently only placed.
 */
public interface PlacedOrder extends Order {

    /**
     * Starts delivering the order.
     * @param droneId drone identifier used to perform delivery
     * @param courierUsername courier username used to perform delivery
     */
    void startDelivering(String droneId, String courierUsername);

    /**
     * Places an order using needed parameters.
     * @param id the {@link OrderIdentifier}
     * @param product the ordered {@link Product}
     * @param client the {@link Client} who placed the order
     * @param placingDate {@link OrderDate} -{@literal >} the date in which the order has been placed
     * @param estimatedArrival {@link OrderDate} -{@literal >} the date in which the order should arrive
     * @return a {@link PlacedOrder}
     */
    @Contract("_, _, _, _, _ -> new")
    static @NotNull PlacedOrder place(final OrderIdentifier id,
                                      final Product product,
                                      final Client client,
                                      final OrderDate placingDate,
                                      final OrderDate estimatedArrival) {
        return new PlacedOrderImpl(id, product, client, placingDate, estimatedArrival);
    }
}
