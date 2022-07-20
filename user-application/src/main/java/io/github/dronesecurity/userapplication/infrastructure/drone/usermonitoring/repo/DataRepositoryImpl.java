/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.drone.usermonitoring.repo;

import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.AccelerometerData;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.CameraData;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.ProximityData;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.SensorData;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.repo.DataRepository;
import io.github.dronesecurity.userapplication.infrastructure.drone.usermonitoring.MonitoringConstants;
import io.github.dronesecurity.userapplication.infrastructure.MongoRepository;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

// TODO ?? separare in 3 diverse repository.
/**
 * Implementation of {@link DataRepository}.
 */
public final class DataRepositoryImpl extends MongoRepository implements DataRepository {

    private static final String PROXIMITY_COLLECION_NAME = "proximityData";
    private static final String ACCELEROMETER_COLLECION_NAME = "accelerometerData";
    private static final String CAMERA_COLLECION_NAME = "cameraData";

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveProximitySnapshot(final ProximityData data) {
        this.saveSnapshot(PROXIMITY_COLLECION_NAME, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAccelerometerSnapshot(final AccelerometerData data) {
        this.saveSnapshot(ACCELEROMETER_COLLECION_NAME, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveCameraSnapshot(final CameraData data) {
        this.saveSnapshot(CAMERA_COLLECION_NAME, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProximityData> retrieveProximityDataHistory(final long orderId) {
        return this.retrieveDataHistory(orderId, PROXIMITY_COLLECION_NAME, ProximityData.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AccelerometerData> retrieveAccelerometerDataHistory(final long orderId) {
        return this.retrieveDataHistory(orderId, ACCELEROMETER_COLLECION_NAME, AccelerometerData.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CameraData> retrieveCameraDataHistory(final long orderId) {
        return this.retrieveDataHistory(orderId, CAMERA_COLLECION_NAME, CameraData.class);
    }

    private <T extends SensorData> void saveSnapshot(final String collectionName, final T data) {
        this.mongo().save(collectionName, JsonObject.mapFrom(data));
    }

    private @Nullable <T extends SensorData> List<T> retrieveDataHistory(final long orderId,
                                                                         final String collectionName,
                                                                         final Class<T> clazz) {
        final JsonObject query = new JsonObject().put(MonitoringConstants.ORDER_ID, orderId);
        return this.waitFutureResult(this.mongo().find(collectionName, query)
                .map(list -> list.stream().map(data -> Json.decodeValue(data.toBuffer(), clazz))
                        .collect(Collectors.toList())));
    }
}
