/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects;

/**
 * Class representing processed proximity sensor data. In this case no transformation of data is needed.
 */
public class ProcessedProximityData extends RawProximityData {

    /**
     * Builds the processed proximity sensor data.
     *
     * @param distance The distance detected by the proximity sensor
     */
    public ProcessedProximityData(final double distance) {
        super(distance);
    }
}
