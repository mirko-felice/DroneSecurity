/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects;

/**
 * Class representing raw camera data, represented as the size of the image received.
 */
public class RawCameraData {

    private final int imageLength;

    /**
     * Builds the raw camera data.
     *
     * @param imageLength The length of the image received
     */
    public RawCameraData(final int imageLength) {
        this.imageLength = imageLength;
    }

    /**
     * Gets the length of the image read.
     * @return the length of the image detected y the camera
     */
    public int getImageLength() {
        return this.imageLength;
    }
}
