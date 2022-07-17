/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.camera.services;

import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.ProcessedCameraData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;

/**
 * Publishes {@link ProcessedCameraData} to a topic.
 */
public interface CameraDataPublisher {

    /**
     * Publishes {@link ProcessedCameraData} to a specific topic.
     * @param orderData data of the order which is currently being delivered
     * @param cameraData camera data to publish
     */
    void publishCameraData(OrderData orderData, ProcessedCameraData cameraData);
}
