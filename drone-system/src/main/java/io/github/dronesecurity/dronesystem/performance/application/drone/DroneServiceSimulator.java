/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.application.drone;

import io.github.dronesecurity.dronesystem.performance.domain.averages.objects.AveragePerformanceData;
import io.github.dronesecurity.dronesystem.performance.domain.drone.entities.PerformanceDrone;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class representing the simulator of the drone service.
 */
public class DroneServiceSimulator {

    private static final int PERFORMANCE_READING_DELAY = 28;

    private final PerformanceDrone drone;
    private final Thread dataMonitoringAgent;
    private final ScheduledExecutorService executor;

    /**
     * Constructor for this service.
     * @throws IOException If one of the specified files does not exist
     */
    public DroneServiceSimulator() throws IOException {
        this.drone = new PerformanceDrone();
        this.dataMonitoringAgent = this.getMonitoringAgent();
        this.executor = Executors.newScheduledThreadPool(2);
    }

    /**
     * Activates the drone and starts reading its sensor data.
     */
    public void startDrone() {
        this.drone.activate();
        this.dataMonitoringAgent.start();
    }

    /**
     * Deactivates the drone.
     */
    public void stopDrone() {
        this.executor.shutdownNow();
        this.drone.deactivate();
    }

    /**
     * Gets the averaged delays of all performance evaluations.
     * @return the map containing the averaged performance delays
     */
    public AveragePerformanceData getAveragePerformance() {
        return this.drone.getAveragePerformances();
    }

    @Contract(" -> new")
    private @NotNull Thread getMonitoringAgent() {
        return new Thread(() -> this.executor.scheduleWithFixedDelay(() -> {
            this.drone.performReading();
            this.drone.publishData();
        }, 0, PERFORMANCE_READING_DELAY, TimeUnit.MILLISECONDS));
    }
}
