/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance;

import io.github.dronesecurity.dronesystem.drone.AccelerometerConstants;
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
     * @param delay Computed delay between the camera data reading and current printing
     */
    public static void printCameraPerformance(final @NotNull PrintWriter cameraWriter,
                                              final @NotNull CameraData cameraData,
                                              final long delay) {
        cameraWriter.println(PACKET_NUMBER_TEXT + cameraData.getIndex() + ":");
        cameraWriter.println("Image size - " + cameraData.getImageSize());
        cameraWriter.println(TIMESTAMP_REPRESENTATION + delay + TIMEUNIT);
        cameraWriter.println();
    }

    /**
     * Prints {@link AccelerometerData} on a specific writer, transforming its timestamp to delay in ms.
     * @param accelerometerWriter The writer on which {@link AccelerometerData} will be printed
     * @param accelerometerPerformanceData Accelerometer data to visualize
     * @param delay Computed delay between the accelerometer data reading and current printing
     */
    public static void printAccelerometerPerformance(final @NotNull PrintWriter accelerometerWriter,
                                                     final @NotNull AccelerometerData accelerometerPerformanceData,
                                                     final long delay) {
        accelerometerWriter.println(PACKET_NUMBER_TEXT + accelerometerPerformanceData.getIndex() + ":");

        final Map<String, Double> accelerometerData = accelerometerPerformanceData.getData();
        accelerometerWriter.println("X - " + accelerometerData.get(AccelerometerConstants.X));
        accelerometerWriter.println("Y - " + accelerometerData.get(AccelerometerConstants.Y));
        accelerometerWriter.println("Z - " + accelerometerData.get(AccelerometerConstants.Z));

        accelerometerWriter.println(TIMESTAMP_REPRESENTATION + delay + TIMEUNIT);
        accelerometerWriter.println();
    }

    /**
     * Prints the performance evaluation of processing the raw data read by the accelerometer into human-readable
     * degree values.
     * @param accelerometerWriter The writer on which processed {@link AccelerometerData} will be printed
     * @param accelerometerPerformanceData Processed accelerometer data to visualize
     * @param delay Computed time required to process accelerometer raw data into readable degrees
     * @param isDelayNanos true whether the delay passed is in nanoseconds, false if it is in milliseconds
     */
    public static void printAccelerometerProcessedDataPerformance(
            final @NotNull PrintWriter accelerometerWriter,
            final @NotNull AccelerometerData accelerometerPerformanceData,
            final long delay,
            final boolean isDelayNanos) {
        accelerometerWriter.println(PACKET_NUMBER_TEXT + accelerometerPerformanceData.getIndex() + ":");

        final Map<String, Double> accelerometerData = accelerometerPerformanceData.getData();
        accelerometerWriter.println("Pitch - " + accelerometerData.get(MqttMessageParameterConstants.PITCH));
        accelerometerWriter.println("Roll - " + accelerometerData.get(MqttMessageParameterConstants.ROLL));
        accelerometerWriter.println("Yaw - " + accelerometerData.get(MqttMessageParameterConstants.YAW));

        accelerometerWriter.println(TIMESTAMP_REPRESENTATION + delay + (isDelayNanos ? " \u03bcs" : TIMEUNIT));
        accelerometerWriter.println();
    }

    /**
     * Prints {@link ProximityData} on a specific writer, transforming its timestamp to delay in ms.
     * @param proximityWriter The writer on which {@link ProximityData} will be printed
     * @param proximityPerformanceData Proximity data to visualize
     * @param delay Computed delay between the proximity sensor data reading and current printing
     */
    public static void printProximityPerformance(final @NotNull PrintWriter proximityWriter,
                                                 final @NotNull ProximityData proximityPerformanceData,
                                                 final long delay) {
        proximityWriter.println(PACKET_NUMBER_TEXT + proximityPerformanceData.getIndex() + ":");
        proximityWriter.println("Distance - " + proximityPerformanceData.getData() + " cm");
        proximityWriter.println(TIMESTAMP_REPRESENTATION + delay + TIMEUNIT);
        proximityWriter.println();
    }

    /**
     * Prints the average delay values of all data.
     * @param resultWriter Writer on which print the results
     * @param readerAverageResults Average results computed by the drone (local delays)
     * @param subscriberAverageResults Average results computed by the subscriber (remote delays)
     */
    public static void printAverageResults(final PrintWriter resultWriter,
                                           final @NotNull Map<String, Long> readerAverageResults,
                                           final @NotNull Map<String, Long> subscriberAverageResults) {

        printIntro(resultWriter, "Performance average results of the last performance execution");

        resultWriter.println("Average results from the reader side (Drone):");
        printAverageDelay(resultWriter, readerAverageResults);
        resultWriter.println();

        resultWriter.println("Average results from the subscriber side (User):");
        printAverageDelay(resultWriter, subscriberAverageResults);
    }

    private static void printAverageDelay(final PrintWriter writer, final @NotNull Map<String, Long> averageResults) {
        averageResults.forEach((dataName, averageDelay) -> writer.println(dataName + " -> " + averageDelay
                + (dataName.contains("Processing") ? " \u03bcs" : TIMEUNIT)));
    }
}
