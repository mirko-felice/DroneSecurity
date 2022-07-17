/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.drone.services;

import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;

/**
 * Publishes the updates regarding drone's moving state.
 */
public interface MovingStatePublisher {

    /**
     * Publishes the change of moving state of the drone to halted.
     * @param orderData The id of the order that the drone is currently delivering
     */
    void droneHalted(OrderData orderData);

    /**
     * Publishes the change of moving state of the drone to moving.
     * @param orderData The id of the order that the drone is currently delivering
     */
    void droneProceeding(OrderData orderData);
}
