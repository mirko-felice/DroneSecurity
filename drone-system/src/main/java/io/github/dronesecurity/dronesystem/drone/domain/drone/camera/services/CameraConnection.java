/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.camera.services;

import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.RawCameraData;

/**
 * Service that provides the connectivity to the camera sensor.
 */
public interface CameraConnection {

    /**
     * Performs the reading of a frame detected by the camera.
     * @return a {@link RawCameraData}
     */
    RawCameraData readCameraData();

    /**
     * Closes the connection with the camera sensor.
     */
    void disconnect();
}
