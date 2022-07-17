/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.application.drone.proximity;

import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.ProcessedProximityData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.RawProximityData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.services.ProximityDataProcessor;
import org.jetbrains.annotations.NotNull;

/**
 * Base implementation of {@link ProximityDataProcessor} that doesn't transform proximity sensor data.
 */
public class ProximityDataProcessorImpl implements ProximityDataProcessor {

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessedProximityData processProximityData(final @NotNull RawProximityData proximityData) {
        return new ProcessedProximityData(proximityData.getDistance());
    }
}
