/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.services;

import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.entities.ProximitySensor;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.ProcessedProximityData;

/**
 * Analyzer that analyzes the data of a {@link ProximitySensor}.
 */
public interface ProximityDataAnalyzer {

    /**
     * Checks if proximity distance is critical or warning giving back an {@link Alert}.
     * @param proximityData data collected by the {@link ProximitySensor}
     * @return an {@link Alert}
     */
    Alert analyzeProximityData(ProcessedProximityData proximityData);
}
