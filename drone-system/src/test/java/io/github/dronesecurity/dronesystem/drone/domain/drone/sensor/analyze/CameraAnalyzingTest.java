/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.analyze;

import io.github.dronesecurity.dronesystem.drone.application.drone.camera.CameraDataAnalyzerImpl;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.ProcessedCameraData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.services.CameraDataAnalyzer;
import io.github.dronesecurity.lib.shared.AlertLevel;
import io.github.dronesecurity.lib.shared.AlertType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link CameraDataAnalyzerImpl}.
 */
class CameraAnalyzingTest {

    private static final int EMPTY_IMAGE = 0;
    private static final int SMALL_IMAGE = 100;
    private static final int MEDIUM_IMAGE = 1_000;
    private static final int BIG_IMAGE = 10_000;
    private static final int VERY_BIG_IMAGE = 100_000;

    /**
     * Tests that camera analyzer gives always stable alert level.
     * @param imageSize size of the image to be analyzed
     */
    @ParameterizedTest
    @ValueSource(ints = {EMPTY_IMAGE, SMALL_IMAGE, MEDIUM_IMAGE, BIG_IMAGE, VERY_BIG_IMAGE})
    void alwaysStableTest(final int imageSize) {
        final CameraDataAnalyzer analyzer = new CameraDataAnalyzerImpl();

        final ProcessedCameraData cameraData = new ProcessedCameraData(imageSize);

        final Alert alert = analyzer.analyzeCameraData(cameraData);

        Assertions.assertEquals(AlertType.CAMERA, alert.getAlertType(), "Alert should be of the CAMERA type.");
        Assertions.assertEquals(AlertLevel.STABLE, alert.getAlertLevel(), "Alert level should ALWAYS be STABLE.");
    }
}
