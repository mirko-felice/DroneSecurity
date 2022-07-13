/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts;

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
}
