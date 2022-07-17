/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.services;

import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.ProcessedProximityData;

/**
 * Publishes {@link ProcessedProximityData} to a topic.
 */
public interface ProximityDataPublisher {

    /**
     * Publishes {@link ProcessedProximityData} to a specific topic.
     * @param orderData data of the order which is currently being delivered
     * @param proximityData proximity sensor data to publish
     */
    void publishProximityData(OrderData orderData, ProcessedProximityData proximityData);
}
