/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.impl;

import io.github.dronesecurity.userapplication.domain.shipping.client.Client;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.Product;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.OrderState;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.SucceededOrder;

/**
 * Implementation of {@link SucceededOrder}.
 */
public final class SucceededOrderImpl extends AbstractOrder implements SucceededOrder {

    /**
     * Build the Succeeded Order.
     * @param id the {@link OrderIdentifier}
     * @param product the ordered {@link Product}
     * @param client the {@link Client} who placed the order
     * @param placingDate {@link OrderDate} -{@literal >} the date in which the order has been placed
     * @param arrival {@link OrderDate} -{@literal >} the date in which the order has been delivered
     */
    public SucceededOrderImpl(final OrderIdentifier id,
                              final Product product,
                              final Client client,
                              final OrderDate placingDate,
                              final OrderDate arrival) {
        super(id, product, client, placingDate, arrival, OrderState.SUCCEEDED);
    }

}
