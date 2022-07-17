/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.application.drone.camera;

import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.ProcessedCameraData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.RawCameraData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.services.CameraDataProcessor;
import org.jetbrains.annotations.NotNull;

/**
 * Base implementation of {@link CameraDataProcessor} that doesn't transform camera data.
 */
public class CameraDataProcessorImpl implements CameraDataProcessor {

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessedCameraData processCameraData(final @NotNull RawCameraData cameraData) {
        return new ProcessedCameraData(cameraData.getImageLength());
    }
}
