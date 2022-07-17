/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.camera.services;

import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.entities.Camera;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.ProcessedCameraData;

/**
 * Analyzer that analyzes the data of a {@link Camera}.
 */
public interface CameraDataAnalyzer {

    /**
     * Checks if the camera data present a critical or warning situation, giving back an {@link Alert}.
     * @param cameraData processed data collected by the {@link Camera}
     * @return an {@link Alert}
     */
    Alert analyzeCameraData(ProcessedCameraData cameraData);
}
