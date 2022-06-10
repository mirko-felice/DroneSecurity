/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.drone.sensordata;

/**
 * POJO class for camera performance data.
 */
public class CameraData extends PerformanceData {

    private final int imageSize;

    /**
     * Builds camera performance data structure.
     * @param index The index of this data reading related to the data flow
     * @param timestamp The timestamp of when the data were read, represented as the difference,
     *                  measured in milliseconds, between the current time and midnight, January 1, 1970 UTC
     * @param imageSize The size of the image read from the camera
     */
    public CameraData(final int index, final long timestamp, final int imageSize) {
        super(index, timestamp);
        this.imageSize = imageSize;
    }

    /**
     * Gets the size of the image read by the camera.
     * @return The size of the image
     */
    public int getImageSize() {
        return this.imageSize;
    }
}
