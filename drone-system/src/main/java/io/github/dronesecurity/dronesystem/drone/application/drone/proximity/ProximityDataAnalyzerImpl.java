/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.application.drone.proximity;

import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.ProcessedProximityData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.services.ProximityDataAnalyzer;
import io.github.dronesecurity.lib.shared.AlertLevel;
import io.github.dronesecurity.lib.shared.AlertType;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link ProximityDataAnalyzer} that informs of the {@link AlertLevel}
 * depending on the distance detected by the proximity sensor.
 */
public class ProximityDataAnalyzerImpl implements ProximityDataAnalyzer {

    private static final double PROXIMITY_WARNING_THRESHOLD = 50;
    private static final double PROXIMITY_CRITICAL_THRESHOLD = 25;

    /**
     * {@inheritDoc}
     */
    @Override
    public Alert analyzeProximityData(final @NotNull ProcessedProximityData proximityData) {
        final double proximityDistance = proximityData.getDistance();
        if (proximityDistance > 0) {
            final boolean isWarning = proximityDistance <= PROXIMITY_WARNING_THRESHOLD;
            final boolean isCritical = proximityDistance <= PROXIMITY_CRITICAL_THRESHOLD;
            if (isCritical)
                return new Alert(AlertType.DISTANCE, AlertLevel.CRITICAL);
            else if (isWarning)
                return new Alert(AlertType.DISTANCE, AlertLevel.WARNING);
        }
        return new Alert(AlertType.DISTANCE, AlertLevel.STABLE);
    }
}
