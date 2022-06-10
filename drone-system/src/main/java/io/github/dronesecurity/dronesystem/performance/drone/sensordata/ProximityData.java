/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.drone.sensordata;

/**
 * POJO class for proximity sensor performance data.
 */
public class ProximityData extends PerformanceData {

    private final double proximity;

    /**
     * Builds performance data structure.
     *
     * @param index The index of this data reading related to the data flow
     * @param timestamp The timestamp of when the data were read, represented as the difference,
     *                  measured in milliseconds, between the current time and midnight, January 1, 1970 UTC
     * @param proximity The data read from the proximity sensor
     */
    public ProximityData(final int index, final long timestamp, final double proximity) {
        super(index, timestamp);
        this.proximity = proximity;
    }

    /**
     * Gets the data read by the proximity sensor.
     * @return The proximity from the nearest obstacle
     */
    public double getData() {
        return this.proximity;
    }
}
