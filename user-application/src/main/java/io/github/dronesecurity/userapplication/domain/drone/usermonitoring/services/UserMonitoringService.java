/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.drone.usermonitoring.services;

/**
 * Service that monitors drone data channels and informs interested parties whenever a data is received.
 */
public interface UserMonitoringService {

    /**
     * Starts monitoring an order.
     * @param orderId order identifier to monitor
     */
    void startOrderMonitoring(long orderId);

    /**
     * Stops monitoring an order.
     * @param orderId order identifier to monitor
     */
    void stopOrderMonitoring(long orderId);
}
