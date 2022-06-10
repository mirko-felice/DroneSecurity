/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance;

import io.github.dronesecurity.dronesystem.performance.drone.sensordata.AccelerometerData;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.CameraData;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.ProximityData;
import io.github.dronesecurity.lib.DateHelper;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.time.Instant;
import java.util.Map;

/**
 * Helper class for printing performance data on files.
 */
public final class OutputHelper {

    private static final String TIMESTAMP_REPRESENTATION = "Delay - ";
    private static final String TIMEUNIT = " ms";
    private static final String PACKET_NUMBER_TEXT = "Metadata for packet #";

    private OutputHelper() { }

    /**
     * Prints the default intro for the file (Should be called at the beginning) with the timestamp of the beginning
     * of data flow and a description.
     * @param outputWriter The writer on which the intro will be printed
     * @param description A brief description of what the data are about
     */
    public static void printIntro(final @NotNull PrintWriter outputWriter, final String description) {
        outputWriter.println(description);
        outputWriter.println(DateHelper.toString(Instant.now()));
        outputWriter.println("---");
        outputWriter.println();
    }

    /**
     * Prints {@link CameraData} on a specific writer, transforming its timestamp to delay in ms.
     * @param cameraWriter The writer on which {@link CameraData} will be printed
     * @param cameraData Camera data to visualize
     */
    public static void printCameraPerformance(final @NotNull PrintWriter cameraWriter,
                                              final @NotNull CameraData cameraData) {
        cameraWriter.println(PACKET_NUMBER_TEXT + cameraData.getIndex() + ":");
        cameraWriter.println("Image size - " + cameraData.getImageSize());
        final long delay = System.currentTimeMillis() - cameraData.getTimestamp();
        cameraWriter.println(TIMESTAMP_REPRESENTATION + delay + TIMEUNIT);
        cameraWriter.println();
    }

    /**
     * Prints {@link AccelerometerData} on a specific writer, transforming its timestamp to delay in ms.
     * @param accelerometerWriter The writer on which {@link AccelerometerData} will be printed
     * @param accelerometerPerformanceData Accelerometer data to visualize
     */
    public static void printAccelerometerPerformance(final @NotNull PrintWriter accelerometerWriter,
                                                     final @NotNull AccelerometerData accelerometerPerformanceData) {
        accelerometerWriter.println(PACKET_NUMBER_TEXT + accelerometerPerformanceData.getIndex() + ":");

        final Map<String, Double> accelerometerData = accelerometerPerformanceData.getData();
        accelerometerWriter.println("X - " + accelerometerData.get("x"));
        accelerometerWriter.println("Y - " + accelerometerData.get("y"));
        accelerometerWriter.println("Z - " + accelerometerData.get("z"));

        final long delay = System.currentTimeMillis() - accelerometerPerformanceData.getTimestamp();
        accelerometerWriter.println(TIMESTAMP_REPRESENTATION + delay + TIMEUNIT);
        accelerometerWriter.println();
    }

    /**
     * Prints the performance evaluation of processing the raw data read by the accelerometer into human-readable
     * degree values.
     * @param accelerometerWriter The writer on which processed {@link AccelerometerData} will be printed
     * @param accelerometerPerformanceData Processed accelerometer data to visualize
     */
    public static void printAccelerometerDataProcessing(final @NotNull PrintWriter accelerometerWriter,
                                                        final @NotNull AccelerometerData accelerometerPerformanceData) {
        accelerometerWriter.println(PACKET_NUMBER_TEXT + accelerometerPerformanceData.getIndex() + ":");

        final Map<String, Double> accelerometerData = accelerometerPerformanceData.getData();
        accelerometerWriter.println("Pitch - " + accelerometerData.get(MqttMessageParameterConstants.PITCH));
        accelerometerWriter.println("Roll - " + accelerometerData.get(MqttMessageParameterConstants.ROLL));
        accelerometerWriter.println("Yaw - " + accelerometerData.get(MqttMessageParameterConstants.YAW));

        final long delay = System.currentTimeMillis() - accelerometerPerformanceData.getTimestamp();
        accelerometerWriter.println(TIMESTAMP_REPRESENTATION + delay + TIMEUNIT);
        accelerometerWriter.println();
    }

    /**
     * Prints {@link ProximityData} on a specific writer, transforming its timestamp to delay in ms.
     * @param proximityWriter The writer on which {@link ProximityData} will be printed
     * @param proximityPerformanceData Proximity data to visualize
     */
    public static void printProximityPerformance(final @NotNull PrintWriter proximityWriter,
                                                     final @NotNull ProximityData proximityPerformanceData) {
        proximityWriter.println(PACKET_NUMBER_TEXT + proximityPerformanceData.getIndex() + ":");
        proximityWriter.println("Distance - " + proximityPerformanceData.getData() + " cm");
        final long delay = System.currentTimeMillis() - proximityPerformanceData.getTimestamp();
        proximityWriter.println(TIMESTAMP_REPRESENTATION + delay + TIMEUNIT);
        proximityWriter.println();
    }
}
