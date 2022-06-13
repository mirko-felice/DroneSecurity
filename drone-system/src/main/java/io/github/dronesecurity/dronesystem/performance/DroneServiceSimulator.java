/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance;

import io.github.dronesecurity.dronesystem.drone.DataProcessor;
import io.github.dronesecurity.dronesystem.performance.drone.DroneTimed;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.AccelerometerData;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.CameraData;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.ProximityData;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
    private final PrintWriter proximityWriter;

    private long totalCameraReaderDelay;
    private long totalCameraReadings;

    private long totalAccelerometerReaderDelay;
    private long totalAccelerometerReadings;

    private long totalAccelerometerAnalyzerDelay;
    private long totalAccelerometerAnalyzerExecutions;

    private long totalProximityReaderDelay;
    private long totalProximityReadings;

    /**
     * Constructor for this service.
     * @param cameraOutputFile The file on which write tracked performance details of the camera
     * @param accelerometerOutputFile The file on which write tracked performance details of the accelerometer
     * @param accelerometerProcessingOutputFile The file on which write performance details of the
     *                                          accelerometer data processing
     * @param proximityOutputFile The file on which write tracked performance details of the proximity sensor
     * @throws IOException If one of the specified files does not exist
     */
    public DroneServiceSimulator(final File cameraOutputFile,
                                 final File accelerometerOutputFile,
                                 final File accelerometerProcessingOutputFile,
                                 final File proximityOutputFile) throws IOException {
        this.drone = new DroneTimed("Performance Drone");
        this.dataMonitoringAgent = this.getMonitoringAgent();
        this.executor = Executors.newScheduledThreadPool(2);

        this.cameraWriter = new PrintWriter(cameraOutputFile, StandardCharsets.UTF_8);
        this.accelerometerWriter = new PrintWriter(accelerometerOutputFile, StandardCharsets.UTF_8);
        this.accelerometerProcessingWriter = new PrintWriter(accelerometerProcessingOutputFile, StandardCharsets.UTF_8);
        this.proximityWriter = new PrintWriter(proximityOutputFile, StandardCharsets.UTF_8);
    }

    /**
     * Activates the drone and starts reading its sensor data.
     */
    public void startDrone() {
        OutputHelper.printIntro(this.cameraWriter, "Camera performance");
        OutputHelper.printIntro(this.accelerometerWriter, "Accelerometer performance");
        OutputHelper.printIntro(this.accelerometerProcessingWriter, "Accelerometer performance (Processed Data)");
        OutputHelper.printIntro(this.proximityWriter, "Proximity performance");

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
        this.proximityWriter.flush();

        this.cameraWriter.close();
        this.accelerometerWriter.close();
        this.accelerometerProcessingWriter.close();
        this.proximityWriter.close();

        this.executor.shutdownNow();
        this.drone.deactivate();
    }

    /**
     * Gets the averaged delays of all performance evaluations.
     * @return the map containing the averaged performance delays
     */
    public Map<String, Long> getAveragePerformance() {
        final Map<String, Long> averagePerformance = new ConcurrentHashMap<>();

        averagePerformance.put(MqttMessageParameterConstants.CAMERA_PARAMETER,
                this.totalCameraReaderDelay / this.totalCameraReadings);
        averagePerformance.put(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER,
                this.totalAccelerometerReaderDelay / this.totalAccelerometerReadings);
        averagePerformance.put(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER + "Processing",
                this.totalAccelerometerAnalyzerDelay / this.totalAccelerometerAnalyzerExecutions);
        averagePerformance.put(MqttMessageParameterConstants.PROXIMITY_PARAMETER,
                this.totalProximityReaderDelay / this.totalProximityReadings);

        return averagePerformance;
    }

    @Contract(" -> new")
    private @NotNull Thread getMonitoringAgent() {
        return new Thread(() -> this.executor.scheduleWithFixedDelay(() -> {
            this.drone.readAllData();
            final CameraData cameraPerformanceData = this.drone.getCameraPerformanceData();
            final AccelerometerData accelerometerPerformanceData = this.drone.getAccelerometerPerformanceData();
            final ProximityData proximityPerformanceData = this.drone.getProximityPerformanceData();

            if (cameraPerformanceData.getTimestamp() != 0) {
                final long delay = System.currentTimeMillis() - cameraPerformanceData.getTimestamp();
                this.totalCameraReaderDelay += delay;
                this.totalCameraReadings++;
                OutputHelper.printCameraPerformance(this.cameraWriter, cameraPerformanceData, delay);
            }
            if (accelerometerPerformanceData.getTimestamp() != 0) {
                final long delay = System.currentTimeMillis() - accelerometerPerformanceData.getTimestamp();
                this.totalAccelerometerReaderDelay += delay;
                this.totalAccelerometerReadings++;
                OutputHelper
                        .printAccelerometerPerformance(this.accelerometerWriter, accelerometerPerformanceData, delay);
            }
            if (proximityPerformanceData.getTimestamp() != 0) {
                final long delay = System.currentTimeMillis() - proximityPerformanceData.getTimestamp();
                this.totalProximityReaderDelay += delay;
                this.totalProximityReadings++;
                OutputHelper.printProximityPerformance(this.proximityWriter, proximityPerformanceData, delay);
            }

            final long start = System.nanoTime();

            if (accelerometerPerformanceData.getTimestamp() != 0) {

                final Map<String, Double> processedAccelerometerValues = new ConcurrentHashMap<>();
                DataProcessor.processAccelerometer(accelerometerPerformanceData.getData())
                        .forEach((key, value) -> processedAccelerometerValues.put(key, value.doubleValue()));

                final long delay = (System.nanoTime() - start) / 1_000;
                this.totalAccelerometerAnalyzerDelay += delay;
                this.totalAccelerometerAnalyzerExecutions++;
                OutputHelper.printAccelerometerProcessedDataPerformance(this.accelerometerProcessingWriter,
                        new AccelerometerData(accelerometerPerformanceData.getIndex(),
                                start,
                                processedAccelerometerValues),
                        delay,
                        true);

                PerformancePublishHelper.publishData(cameraPerformanceData,
                        new AccelerometerData(accelerometerPerformanceData.getIndex(),
                                accelerometerPerformanceData.getTimestamp(),
                                processedAccelerometerValues),
                        proximityPerformanceData);
            }
        }, 0, PERFORMANCE_READING_DELAY, TimeUnit.MILLISECONDS));
    }
}
