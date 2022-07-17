/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.camera.objects;

import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.RawCameraData;
import io.github.dronesecurity.dronesystem.performance.domain.drone.objects.PerformanceData;

/**
 * POJO class for camera performance data.
 */
public class CameraPerformanceData extends PerformanceData {

    private final RawCameraData cameraData;

    /**
     * Builds camera performance data structure.
     * @param index The index of this data reading related to the data flow
     * @param timestamp The timestamp of when the data were read, represented as the difference,
     *                  measured in milliseconds, between the current time and midnight, January 1, 1970 UTC
     * @param cameraData The camera data read by the camera
     */
    public CameraPerformanceData(final int index, final long timestamp, final RawCameraData cameraData) {
        super(index, timestamp);
        this.cameraData = cameraData;
    }

    /**
     * Gets the size of the image read by the camera.
     * @return The size of the image
     */
    public RawCameraData getCameraData() {
        return this.cameraData;
    }
}
