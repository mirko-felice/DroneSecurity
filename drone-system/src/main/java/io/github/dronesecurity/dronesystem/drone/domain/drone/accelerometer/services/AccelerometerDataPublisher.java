/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.services;

import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.ProcessedAccelerometerData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;

/**
 * Publishes {@link ProcessedAccelerometerData} to a topic.
 */
public interface AccelerometerDataPublisher {

    /**
     * Publishes {@link ProcessedAccelerometerData} to a specific topic.
     * @param orderData data of the order which is currently being delivered
     * @param accelerometerData accelerometer data to publish
     */
    void publishAccelerometerData(OrderData orderData, ProcessedAccelerometerData accelerometerData);
}
