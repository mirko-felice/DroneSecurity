/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.domain.proximity.objects;

import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.RawProximityData;
import io.github.dronesecurity.dronesystem.performance.domain.drone.objects.PerformanceData;

/**
 * POJO class for proximity sensor performance data.
 */
public class ProximityPerformanceData extends PerformanceData {

    private final RawProximityData proximityData;

    /**
     * Builds performance data structure.
     *
     * @param index The index of this data reading related to the data flow
     * @param timestamp The timestamp of when the data were read, represented as the difference,
     *                  measured in milliseconds, between the current time and midnight, January 1, 1970 UTC
     * @param proximity The data read from the proximity sensor
     */
    public ProximityPerformanceData(final int index, final long timestamp, final RawProximityData proximity) {
        super(index, timestamp);
        this.proximityData = proximity;
    }

    /**
     * Gets the data read by the proximity sensor.
     * @return The proximity from the nearest obstacle
     */
    public RawProximityData getProximityData() {
        return this.proximityData;
    }
}
