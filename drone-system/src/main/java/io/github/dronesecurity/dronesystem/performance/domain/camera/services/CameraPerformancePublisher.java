/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.camera.services;

import io.github.dronesecurity.dronesystem.performance.domain.camera.entities.PerformanceCamera;
import io.github.dronesecurity.dronesystem.performance.domain.camera.objects.CameraPerformanceData;

/**
 * Publisher of the {@link CameraPerformanceData}.
 */
public interface CameraPerformancePublisher {

    /**
     * Builds and publishes data read by the camera to its topic.
     * @param cameraPerformanceData data of the {@link PerformanceCamera} to be sent
     */
    void publishCamera(CameraPerformanceData cameraPerformanceData);
}
