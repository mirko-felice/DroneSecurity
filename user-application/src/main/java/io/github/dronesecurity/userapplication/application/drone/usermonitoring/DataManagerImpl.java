/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.drone.usermonitoring;

import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.AccelerometerData;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.CameraData;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.ProximityData;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.repo.DataRepository;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.services.DataManager;

import java.util.List;

/**
 * Implementation of {@link DataManager}.
 */
public final class DataManagerImpl implements DataManager {

    private final DataRepository dataRepository;

    /**
     * Build the service, automatically subscribed to all topics.
     * @param dataRepository {@link DataRepository} to retrieve data from
     */
    public DataManagerImpl(final DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProximityData> retrieveProximityDataHistory(final long orderId) {
        return this.dataRepository.retrieveProximityDataHistory(orderId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AccelerometerData> retrieveAccelerometerDataHistory(final long orderId) {
        return this.dataRepository.retrieveAccelerometerDataHistory(orderId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CameraData> retrieveCameraDataHistory(final long orderId) {
        return this.dataRepository.retrieveCameraDataHistory(orderId);
    }

}
