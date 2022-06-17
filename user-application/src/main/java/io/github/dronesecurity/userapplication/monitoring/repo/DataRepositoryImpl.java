/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.monitoring.repo;

import io.github.dronesecurity.userapplication.monitoring.entities.DroneData;
import io.github.dronesecurity.userapplication.monitoring.utilities.DataConstants;
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
    public void save(final DroneData data) {
        VertxHelper.MONGO_CLIENT.save(COLLECION_NAME, JsonObject.mapFrom(data));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<List<DroneData>> retrieveDataHistory(final long orderId) {
        final JsonObject query = new JsonObject().put(DataConstants.ORDER_ID, orderId);
        return VertxHelper.MONGO_CLIENT.find(COLLECION_NAME, query)
                .map(list -> list.stream().map(data -> Json.decodeValue(data.toBuffer(), DroneData.class))
                        .collect(Collectors.toList()));
    }
}
