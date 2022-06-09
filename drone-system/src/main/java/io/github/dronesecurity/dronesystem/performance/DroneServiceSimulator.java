/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance;

import io.github.dronesecurity.dronesystem.drone.DataProcessor;
import io.github.dronesecurity.dronesystem.performance.drone.DroneTimed;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.AccelerometerData;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.CameraData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class representing the simulator of the drone service.
 */
public class DroneServiceSimulator {

    private static final int PERFORMANCE_READING_DELAY = 28;

    private final DroneTimed drone;
    private final Thread dataMonitoringAgent;
    private final ScheduledExecutorService executor;
    private final PrintWriter accelerometerWriter;
    private final PrintWriter cameraWriter;
    private final PrintWriter accelerometerProcessingWriter;

    /**
     * Constructor for this service.
     * @param cameraOutputFile The file on which write tracked performance details of the camera
     * @param accelerometerOutputFile The file on which write tracked performance details of the accelerometer
     * @param accelerometerProcessingOutputFile The file on which write performance details of the
     *                                          accelerometer data processing
     * @throws IOException If the specified file does not exist
     */
    public DroneServiceSimulator(final File cameraOutputFile,
                                 final File accelerometerOutputFile,
                                 final File accelerometerProcessingOutputFile) throws IOException {
        this.drone = new DroneTimed();
        this.dataMonitoringAgent = this.getMonitoringAgent();
        this.executor = Executors.newScheduledThreadPool(2);

        this.cameraWriter = new PrintWriter(cameraOutputFile, StandardCharsets.UTF_8);
        this.accelerometerWriter = new PrintWriter(accelerometerOutputFile, StandardCharsets.UTF_8);
        this.accelerometerProcessingWriter = new PrintWriter(accelerometerProcessingOutputFile, StandardCharsets.UTF_8);
    }

    /**
     * Activates the drone and starts reading its sensor data.
     */
    public void startDrone() {
        OutputHelper.printIntro(this.cameraWriter, "Camera performance");
        OutputHelper.printIntro(this.accelerometerWriter, "Accelerometer performance");
        OutputHelper.printIntro(this.accelerometerProcessingWriter, "Accelerometer performance (Processed Data)");

        this.drone.activate();
        this.dataMonitoringAgent.start();
    }

    /**
     * Deactivates the drone.
     */
    public void stopDrone() {
        this.cameraWriter.flush();
        this.accelerometerWriter.flush();
        this.accelerometerProcessingWriter.flush();

        this.cameraWriter.close();
        this.accelerometerWriter.close();
        this.accelerometerProcessingWriter.close();

        this.executor.shutdownNow();
        this.drone.deactivate();
    }

    @Contract(" -> new")
    private @NotNull Thread getMonitoringAgent() {
        return new Thread(() -> this.executor.scheduleWithFixedDelay(() -> {
            this.drone.readAllData();
            final CameraData cameraPerformanceData = this.drone.getCameraPerformanceData();
            final AccelerometerData accelerometerPerformanceData = this.drone.getAccelerometerPerformanceData();

            if (cameraPerformanceData.getTimestamp() != 0)
                OutputHelper.printCameraPerformance(this.cameraWriter, cameraPerformanceData);
            if (accelerometerPerformanceData.getTimestamp() != 0)
                OutputHelper.printAccelerometerPerformance(this.accelerometerWriter, accelerometerPerformanceData);

            final long start = System.currentTimeMillis();

            if (accelerometerPerformanceData.getTimestamp() != 0) {

                final AccelerometerData processedAccelerometerData =
                        new AccelerometerData(accelerometerPerformanceData.getIndex(), start, new DataProcessor()
                                .processAccelerometer(accelerometerPerformanceData.getData()));
                OutputHelper.printAccelerometerDataProcessing(this.accelerometerProcessingWriter,
                        processedAccelerometerData);

                PerformancePublishHelper.publishData(cameraPerformanceData, processedAccelerometerData);
            }
        }, 0, PERFORMANCE_READING_DELAY, TimeUnit.MILLISECONDS));

    }
}
