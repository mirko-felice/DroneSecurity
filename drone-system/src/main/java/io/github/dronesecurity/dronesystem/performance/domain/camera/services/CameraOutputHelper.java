/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.camera.services;

import io.github.dronesecurity.dronesystem.performance.domain.camera.objects.CameraPerformanceData;

/**
 * Output helper that prints camera performance data to a writer.
 */
public interface CameraOutputHelper {

    /**
     * Prints camera performance data.
     *
     * @param cameraPerformanceData the data to be printed
     * @param delay computed delay between the reading of the data and the current print
     */
    void printCameraPerformance(CameraPerformanceData cameraPerformanceData,
                                long delay);

    /**
     * Flushes and closes its writer.
     */
    void flushAndClose();
}
