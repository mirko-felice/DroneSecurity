/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.monitoring.repo;

import io.github.dronesecurity.userapplication.monitoring.entities.MonitoringDroneData;
import io.github.dronesecurity.userapplication.monitoring.utilities.MonitoringConstants;
import io.github.dronesecurity.userapplication.utilities.VertxHelper;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link DataRepository}.
 */
public final class DataRepositoryImpl implements DataRepository {

    private static final String COLLECION_NAME = "droneData";
    private static DataRepository singleton;

    private DataRepositoryImpl() { }

    /**
     * Gets the singleton instance.
     * @return the singleton
     */
    public static DataRepository getInstance() {
        synchronized (DataRepositoryImpl.class) {
            if (singleton == null)
                singleton = new DataRepositoryImpl();
            return singleton;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(final MonitoringDroneData data) {
        VertxHelper.MONGO_CLIENT.save(COLLECION_NAME, JsonObject.mapFrom(data));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<List<MonitoringDroneData>> retrieveDataHistory(final long orderId) {
        final JsonObject query = new JsonObject().put(MonitoringConstants.ORDER_ID, orderId);
        return VertxHelper.MONGO_CLIENT.find(COLLECION_NAME, query)
                .map(list -> list.stream().map(data -> Json.decodeValue(data.toBuffer(), MonitoringDroneData.class))
                        .collect(Collectors.toList()));
    }
}
