/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.proximity.services;

import io.github.dronesecurity.dronesystem.performance.domain.proximity.objects.ProximityPerformanceData;

/**
 * Output helper that prints proximity sensor performance data to a writer.
 */
public interface ProximityOutputHelper {

    /**
     * Prints proximity sensor performance data.
     *
     * @param proximityPerformanceData the data to be printed
     * @param delay computed delay between the reading of the data and the current print
     */
    void printProximityPerformance(ProximityPerformanceData proximityPerformanceData,
                                   long delay);

    /**
     * Flushes and closes its writer.
     */
    void flushAndClose();
}
