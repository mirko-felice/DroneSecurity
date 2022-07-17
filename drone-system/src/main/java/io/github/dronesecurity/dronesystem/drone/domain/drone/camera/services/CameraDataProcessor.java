/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.camera.services;

import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.ProcessedCameraData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.RawCameraData;

/**
 * Processor that transforms {@link RawCameraData} into {@link ProcessedCameraData}.
 */
public interface CameraDataProcessor {

    /**
     * Elaborate raw camera data to convert into processed data.
     * @param cameraData raw camera data
     * @return a new processed camera data
     */
    ProcessedCameraData processCameraData(RawCameraData cameraData);
}
