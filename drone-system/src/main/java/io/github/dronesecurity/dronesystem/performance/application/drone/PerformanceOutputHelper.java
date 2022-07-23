/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.application.drone;

import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.ProcessedAccelerometerData;
import io.github.dronesecurity.dronesystem.performance.domain.accelerometer.objects.ProcessedAccelerometerPerformanceData;
import io.github.dronesecurity.dronesystem.performance.domain.accelerometer.objects.RawAccelerometerPerformanceData;
import io.github.dronesecurity.dronesystem.performance.domain.averages.objects.AveragePerformanceData;
import io.github.dronesecurity.dronesystem.performance.domain.camera.objects.CameraPerformanceData;
import io.github.dronesecurity.dronesystem.performance.domain.proximity.objects.ProximityPerformanceData;
import io.github.dronesecurity.lib.utilities.DateHelper;
import io.github.dronesecurity.lib.connection.MqttMessageParameterConstants;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.time.Instant;

/**
 * Helper class for printing performance data on files.
 */
public final class PerformanceOutputHelper {

    private static final String COLON = ":";
    private static final String ARROW = " -> ";
    private static final String TIMESTAMP_REPRESENTATION = "Delay - ";
    private static final String TIMEUNIT = " ms";
    private static final String PACKET_NUMBER_TEXT = "Metadata for packet #";

    private PerformanceOutputHelper() { }

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
     * Prints {@link CameraPerformanceData} on a specific writer, transforming its timestamp to delay in ms.
     * @param cameraWriter The writer on which {@link CameraPerformanceData} will be printed
     * @param cameraPerformanceData Camera data to visualize
     * @param delay Computed delay between the camera data reading and current printing
     */
    public static void printCameraPerformance(final @NotNull PrintWriter cameraWriter,
                                              final @NotNull CameraPerformanceData cameraPerformanceData,
                                              final long delay) {
        cameraWriter.println(PACKET_NUMBER_TEXT + cameraPerformanceData.getIndex() + COLON);
        cameraWriter.println("Image size - " + cameraPerformanceData.getCameraData().getImageLength());
        cameraWriter.println(TIMESTAMP_REPRESENTATION + delay + TIMEUNIT);
        cameraWriter.println();
    }

    /**
     * Prints the performance evaluation of processing the raw data read by the accelerometer into human-readable
     * degree values.
     * @param accelerometerWriter The writer on which processed {@link RawAccelerometerPerformanceData} will be printed
     * @param accelerometerPerformanceData Processed accelerometer data to visualize
     * @param delay Computed time required to process accelerometer raw data into readable degrees
     */
    public static void printAccelerometerPerformance(
            final @NotNull PrintWriter accelerometerWriter,
            final @NotNull ProcessedAccelerometerPerformanceData accelerometerPerformanceData,
            final long delay) {
        accelerometerWriter.println(PACKET_NUMBER_TEXT + accelerometerPerformanceData.getIndex() + COLON);

        final ProcessedAccelerometerData accelerometerData = accelerometerPerformanceData.getAccelerometerData();
        accelerometerWriter.println("Pitch - " + accelerometerData.getPitch());
        accelerometerWriter.println("Roll - " + accelerometerData.getRoll());
        accelerometerWriter.println("Yaw - " + accelerometerData.getYaw());

        accelerometerWriter.println(TIMESTAMP_REPRESENTATION + delay + TIMEUNIT);
        accelerometerWriter.println();
    }

    /**
     * Prints {@link ProximityPerformanceData} on a specific writer, transforming its timestamp to delay in ms.
     * @param proximityWriter The writer on which {@link ProximityPerformanceData} will be printed
     * @param proximityPerformanceData Proximity data to visualize
     * @param delay Computed delay between the proximity sensor data reading and current printing
     */
    public static void printProximityPerformance(final @NotNull PrintWriter proximityWriter,
                                                 final @NotNull ProximityPerformanceData proximityPerformanceData,
                                                 final long delay) {
        proximityWriter.println(PACKET_NUMBER_TEXT + proximityPerformanceData.getIndex() + COLON);
        proximityWriter.println("Distance - " + proximityPerformanceData.getProximityData() + " cm");
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
                                           final @NotNull AveragePerformanceData readerAverageResults,
                                           final @NotNull AveragePerformanceData subscriberAverageResults) {

        printIntro(resultWriter, "Performance average results of the last performance execution");

        resultWriter.println("Average results from the reader side (Drone):");
        printAverageDelay(resultWriter, readerAverageResults);
        resultWriter.println();

        resultWriter.println("Average results from the subscriber side (User):");
        printAverageDelay(resultWriter, subscriberAverageResults);
    }

    private static void printAverageDelay(final @NotNull PrintWriter writer,
                                          final @NotNull AveragePerformanceData averageResults) {
        writer.println(MqttMessageParameterConstants.PROXIMITY_PARAMETER + ARROW
                + averageResults.getAverageProximityDelay());
        writer.println(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER + ARROW
                + averageResults.getAverageAccelerometerDelay());
        writer.println(MqttMessageParameterConstants.CAMERA_PARAMETER + ARROW
                + averageResults.getAverageCameraDelay());
    }
}
