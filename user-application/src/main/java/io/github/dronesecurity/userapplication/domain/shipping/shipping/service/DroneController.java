/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.service;

import io.github.dronesecurity.lib.shared.DrivingMode;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;

/**
 * Domain Service dedicated to perform actions on a drone.
 */
public interface DroneController {

    /**
     * Call back a drone using the {@link OrderIdentifier} of the order currently delivering.
     * @param orderId {@link OrderIdentifier} to identify the drone
     */
    void callBack(OrderIdentifier orderId);

    /**
     * Change driving mode of a drone using the {@link OrderIdentifier} of the order currently delivering.
     * @param orderId {@link OrderIdentifier} to identify the drone
     * @param drivingMode new {@link DrivingMode} to apply
     */
    void changeDrivingMode(OrderIdentifier orderId, DrivingMode drivingMode);

    /**
     * Make a drone proceeding.
     * @param orderId {@link OrderIdentifier} to identify the drone
     */
    void proceed(OrderIdentifier orderId);

    /**
     * Halts a drone.
     * @param orderId {@link OrderIdentifier} to identify the drone
     */
    void halt(OrderIdentifier orderId);
}
