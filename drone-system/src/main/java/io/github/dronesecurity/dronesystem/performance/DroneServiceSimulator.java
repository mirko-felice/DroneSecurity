/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance;

import io.github.dronesecurity.dronesystem.performance.drone.DroneTimed;
import io.github.dronesecurity.lib.DateHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class representing the simulator of the drone service.
 */
public class DroneServiceSimulator {

    private static final int PERFORMANCE_READING_DELAY = 25;

    private final DroneTimed drone;
    private final Thread dataMonitoringAgent;
    private final ScheduledExecutorService executor;
    private int lastCameraIndex = -1;
    private final PrintWriter writer;

    /**
     * Constructor for this service.
     * @param outputFile The file on which write tracked performance details
     * @throws IOException If the specified file does not exist
     */
    public DroneServiceSimulator(final File outputFile) throws IOException {
        this.drone = new DroneTimed();
        this.dataMonitoringAgent = this.getMonitoringAgent();
        this.executor = Executors.newScheduledThreadPool(2);

        this.writer = new PrintWriter(outputFile, StandardCharsets.UTF_8);
    }

    /**
     * Activates the drone and starts reading its sensor data.
     */
    public void startDrone() {
        this.writer.println(DateHelper.toString(Instant.now()));
        this.writer.println("---");
        this.writer.println();

        this.drone.activate();
        this.dataMonitoringAgent.start();
    }

    /**
     * Deactivates the drone.
     */
    public void stopDrone() {
        this.writer.flush();
        this.writer.close();
        this.executor.shutdownNow();
        this.drone.deactivate();
    }

    @Contract(" -> new")
    private @NotNull Thread getMonitoringAgent() {
        return new Thread(() -> {
            this.executor.scheduleWithFixedDelay(() -> {
                this.drone.readAllData();
                final int index = this.drone.getCameraReadingIndex();
                final Byte[] cameraSensorData = this.drone.getCameraSensorData();

                if (index != this.lastCameraIndex && cameraSensorData.length > 0) {

                    this.lastCameraIndex = index;
                    final long timestamp = this.drone.getCameraReadingTimestamp();

                    this.writer.println("Metadata for packet #" + index + ":");
                    this.writer.println("Image size - " + cameraSensorData.length);
                    final long delay = System.currentTimeMillis() - timestamp;
                    this.writer.println("Delay - " + delay + " ms");
                    this.writer.println();

                    PerformancePublishHelper.publishCamera(cameraSensorData, timestamp);
                }
            }, 0, PERFORMANCE_READING_DELAY, TimeUnit.MILLISECONDS);
        });
    }
}
