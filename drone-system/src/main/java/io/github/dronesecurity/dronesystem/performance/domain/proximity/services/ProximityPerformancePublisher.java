/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.proximity.services;

import io.github.dronesecurity.dronesystem.performance.domain.proximity.entities.PerformanceProximity;
import io.github.dronesecurity.dronesystem.performance.domain.proximity.objects.ProximityPerformanceData;

/**
 * Publisher of the {@link ProximityPerformanceData}.
 */
public interface ProximityPerformancePublisher {

    /**
     * Builds and publishes data read by the proximity sensor to its topic.
     * @param proximityPerformanceData data of the {@link PerformanceProximity} to be sent
     */
    void publishProximity(ProximityPerformanceData proximityPerformanceData);
}
