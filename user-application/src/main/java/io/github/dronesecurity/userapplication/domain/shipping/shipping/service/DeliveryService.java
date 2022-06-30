/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.service;

import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.DeliveringOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.FailedOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.PlacedOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;

/**
 * Domain Service dedicated to delivery operations.
 */
public interface DeliveryService {

    /**
     * Performs the delivery of a {@link PlacedOrder}.
     * @param order {@link PlacedOrder} to perform delivery on
     * @param droneId drone identifier to perform delivery on
     */
    void performDelivery(PlacedOrder order, String droneId);

    /**
     * Succeeds the delivery.
     * @param order current {@link DeliveringOrder} to succeed
     */
    void succeedDelivery(DeliveringOrder order);

    /**
     * Fails the delivery.
     * @param order current {@link DeliveringOrder} to fail
     */
    void failDelivery(DeliveringOrder order);

    /**
     * Reschedules a delivery.
     * @param order {@link FailedOrder} to reschedule
     * @param newEstimatedArrival new {@link OrderDate} used to reschedule the order
     */
    void rescheduleDelivery(FailedOrder order, OrderDate newEstimatedArrival);

}
