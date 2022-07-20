/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.drone.usermonitoring.services;

import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.AccelerometerData;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.CameraData;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.ProximityData;

import java.util.List;

/**
 * Service that manages all sensor data detected related to the orders.
 */
public interface DataManager {

    /**
     * Retrieves proximity data history related to an order.
     * @param orderId order identifier to retrieve data from
     * @return the {@link List} of {@link ProximityData}
     */
    List<ProximityData> retrieveProximityDataHistory(long orderId);

    /**
     * Retrieves accelerometer data history related to an order.
     * @param orderId order identifier to retrieve data from
     * @return the {@link List} of {@link AccelerometerData}
     */
    List<AccelerometerData> retrieveAccelerometerDataHistory(long orderId);

    /**
     * Retrieves camera data history related to an order.
     * @param orderId order identifier to retrieve data from
     * @return the {@link List} of {@link CameraData}
     */
    List<CameraData> retrieveCameraDataHistory(long orderId);
}
