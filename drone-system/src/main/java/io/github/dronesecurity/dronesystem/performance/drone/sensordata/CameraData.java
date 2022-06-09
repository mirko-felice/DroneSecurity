/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.drone.sensordata;

/**
 * POJO class for camera performance data.
 */
public class CameraData extends PerformanceData {

    private final Byte[] image;

    /**
     * Builds camera performance data structure.
     * @param index The index of this data reading related to the data flow
     * @param timestamp The timestamp of when the data were read, represented as the difference,
     *                  measured in milliseconds, between the current time and midnight, January 1, 1970 UTC
     * @param image The image read from the camera
     */
    public CameraData(final int index, final long timestamp, final Byte[] image) {
        super(index, timestamp);
        this.image = image.clone();
    }

    /**
     * Gets the image read by the camera.
     * @return The image as a byte array
     */
    public Byte[] getImage() {
        return this.image.clone();
    }
}
