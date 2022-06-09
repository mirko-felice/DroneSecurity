/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.drone.sensordata;

/**
 * POJO class for a generic performance data.
 */
public class PerformanceData {

    private final int index;
    private final long timestamp;

    /**
     * Builds performance data structure.
     * @param index The index of this data reading related to the data flow
     * @param timestamp The timestamp of when the data were read, represented as the difference,
     *                  measured in milliseconds, between the current time and midnight, January 1, 1970 UTC
     */
    protected PerformanceData(final int index, final long timestamp) {
        this.index = index;
        this.timestamp = timestamp;
    }

    /**
     * Gets the index of this data in the data flow.
     * @return The index of this data reading
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Gets the timestamp of when the data were read.
     * @return The timestamp value, represented as the difference, measured in milliseconds,
     *         between the current time and midnight, January 1, 1970 UTC
     */
    public long getTimestamp() {
        return this.timestamp;
    }
}
