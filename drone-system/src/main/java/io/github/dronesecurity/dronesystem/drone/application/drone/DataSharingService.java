/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.application.drone;

import io.github.dronesecurity.dronesystem.drone.domain.drone.drone.entities.Drone;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service that constantly reads data from sensors and publishes them.
 */
public class DataSharingService {

    private static final long ANALIZER_SLEEP_DURATION = 200;

    private final Drone drone;
    private final ScheduledExecutorService dataExecutor;

    /**
     * Constructs the service with a specific drone.
     *
     * @param drone the drone assigned to the service
     */
    public DataSharingService(final Drone drone) {
        this.drone = drone;
        this.dataExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Activates the service.
     */
    public void start() {
        this.dataExecutor.scheduleWithFixedDelay(() -> {
                this.drone.performReading();
                this.drone.publishSensorData();
            }, 0, ANALIZER_SLEEP_DURATION, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the service.
     */
    public void stop() {
        this.dataExecutor.shutdownNow();
    }
}
