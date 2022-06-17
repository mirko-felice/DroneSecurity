/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.monitoring.repo;

import io.github.dronesecurity.userapplication.monitoring.entities.DroneData;
import io.vertx.core.Future;

import java.util.List;

/**
 * Repository to perform actions on {@link DroneData} entity.
 */
public interface DataRepository {

    /**
     * Saves the drone data instance.
     * @param data {@link DroneData} to save
     */
    void save(DroneData data);

    /**
     * Retrieves data history related to an order.
     * @param orderId order identifier to retrieve data from
     * @return the {@link Future} containing the {@link List} of {@link DroneData}
     */
    Future<List<DroneData>> retrieveDataHistory(long orderId);

    /**
     * Gets the singleton instance.
     * @return the singleton
     */
    static DataRepository getInstance() {
        return DataRepositoryImpl.getInstance();
    }

}
