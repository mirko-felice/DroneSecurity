/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects;

/**
 * Class representing processed camera data. In this case no transformation of data is needed.
 */
public class ProcessedCameraData extends RawCameraData {

    /**
     * Builds the processed camera data.
     *
     * @param imageLength The length of the image
     */
    public ProcessedCameraData(final int imageLength) {
        super(imageLength);
    }
}
