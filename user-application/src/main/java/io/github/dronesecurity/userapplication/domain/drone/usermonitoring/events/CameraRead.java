/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.drone.usermonitoring.events;

import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.CameraData;
import io.github.dronesecurity.userapplication.events.DomainEvent;

/**
 * The event to be raised when the drone publishes its camera sensor data.
 */
public final class CameraRead implements DomainEvent {

    private final CameraData cameraData;

    /**
     * Instantiates the Camera data Read event.
     *
     * @param camera data read by the camera
     */
    public CameraRead(final CameraData camera) {
        this.cameraData = camera;
    }

    /**
     * Gets camera data.
     *
     * @return value read by the camera
     */
    public CameraData getCameraData() {
        return this.cameraData;
    }
}
