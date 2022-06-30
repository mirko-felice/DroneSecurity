/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.monitoring.repo;

import io.github.dronesecurity.userapplication.domain.monitoring.entities.MonitoringDroneData;
import io.vertx.core.Future;

import java.util.List;

/**
 * Repository to perform actions on {@link MonitoringDroneData} entity.
 */
public interface DataRepository {

    /**
     * Saves the drone data instance.
     * @param data {@link MonitoringDroneData} to save
     */
    void save(MonitoringDroneData data);

    /**
     * Retrieves data history related to an order.
     * @param orderId order identifier to retrieve data from
     * @return the {@link Future} containing the {@link List} of {@link MonitoringDroneData}
     */
    Future<List<MonitoringDroneData>> retrieveDataHistory(long orderId);

    /**
     * Gets the singleton instance.
     * @return the singleton
     */
    static DataRepository getInstance() {
        return DataRepositoryImpl.getInstance();
    }

}
