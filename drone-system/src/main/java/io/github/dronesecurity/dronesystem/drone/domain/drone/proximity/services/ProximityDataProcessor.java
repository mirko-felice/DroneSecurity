/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.services;

import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.ProcessedProximityData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.RawProximityData;

/**
 * Processor that transforms {@link RawProximityData} into {@link ProcessedProximityData}.
 */
public interface ProximityDataProcessor {

    /**
     * Elaborate raw proximity sensor data into processed proximity sensor data.
     * @param proximityData raw proximity sensor data
     * @return a new processed proximity sensor data
     */
    ProcessedProximityData processProximityData(RawProximityData proximityData);
}
