/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.application.drone.camera;

import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.ProcessedCameraData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.services.CameraDataAnalyzer;
import io.github.dronesecurity.lib.shared.AlertLevel;
import io.github.dronesecurity.lib.shared.AlertType;

/**
 * Base implementation of {@link CameraDataAnalyzer} that always gives a stable alert level as result.
 */
public class CameraDataAnalyzerImpl implements CameraDataAnalyzer {

    /**
     * {@inheritDoc}
     */
    @Override
    public Alert analyzeCameraData(final ProcessedCameraData cameraData) {
        return new Alert(AlertType.CAMERA, AlertLevel.STABLE);
    }
}
