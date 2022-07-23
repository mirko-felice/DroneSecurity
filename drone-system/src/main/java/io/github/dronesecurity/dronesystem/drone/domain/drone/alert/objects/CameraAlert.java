/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects;

import io.github.dronesecurity.lib.shared.AlertLevel;
import io.github.dronesecurity.lib.shared.AlertType;

/**
 * Alert representing the alert specific to the camera.
 */
public class CameraAlert extends Alert {

    private final int imageSize;

    /**
     * Builds the Alert with its own type and level.
     * @param alertType The type of the alert
     * @param alertLevel The gravity of the alert
     * @param imageSize The size of the image detected by the camera
     */
    public CameraAlert(final AlertType alertType, final AlertLevel alertLevel, final int imageSize) {
        super(alertType, alertLevel);
        this.imageSize = imageSize;
    }

    /**
     * Gets the image size related to the alert.
     * @return The image size of the alert
     */
    public int getImageSize() {
        return this.imageSize;
    }
}
