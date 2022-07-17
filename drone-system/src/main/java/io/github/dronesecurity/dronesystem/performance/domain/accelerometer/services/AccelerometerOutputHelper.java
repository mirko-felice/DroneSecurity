/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.accelerometer.services;

import io.github.dronesecurity.dronesystem.performance.domain.accelerometer.objects.ProcessedAccelerometerPerformanceData;

/**
 * Output helper that prints accelerometer performance data to a writer.
 */
public interface AccelerometerOutputHelper {

    /**
     * Prints accelerometer performance data.
     *
     * @param accelerometerPerformanceData the data to be printed
     * @param delay computed delay between the reading of the data and the current print
     */
    void printAccelerometerPerformance(ProcessedAccelerometerPerformanceData accelerometerPerformanceData,
                                       long delay);

    /**
     * Flushes and closes its writer.
     */
    void flushAndClose();
}
