/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.application.accelerometer;

import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.ProcessedAccelerometerData;
import io.github.dronesecurity.dronesystem.performance.domain.accelerometer.objects.ProcessedAccelerometerPerformanceData;
import io.github.dronesecurity.dronesystem.performance.domain.accelerometer.services.AccelerometerOutputHelper;
import io.github.dronesecurity.lib.utilities.DateHelper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.time.Instant;

/**
 * Implementation of the {@link AccelerometerOutputHelper}.
 */
public class AccelerometerOutputHelperImpl implements AccelerometerOutputHelper {

    private static final String SEP = FileSystems.getDefault().getSeparator();
    private static final String RESULT_FOLDER = "performance";
    private static final String ACCELEROMENTER_PERFORMANCE_READER_FILE_NAME =
            RESULT_FOLDER + SEP + "accelerometer_reader.txt";

    private static final String ACCELEROMETER_DESCRIPTION = "Accelerometer performance";
    private static final String TIMESTAMP_REPRESENTATION = "Delay - ";
    private static final String TIMEUNIT = " ms";
    private static final String PACKET_NUMBER_TEXT = "Metadata for packet #";

    private final PrintWriter writer;

    /**
     * Builds the output helper.
     * @throws IOException Whether the writer cannot be created
     */
    public AccelerometerOutputHelperImpl() throws IOException {
        this.writer = new PrintWriter(ACCELEROMENTER_PERFORMANCE_READER_FILE_NAME, StandardCharsets.UTF_8);
        this.printIntro();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printAccelerometerPerformance(
            final @NotNull ProcessedAccelerometerPerformanceData accelerometerPerformanceData,
            final long delay) {

        this.writer.println(PACKET_NUMBER_TEXT + accelerometerPerformanceData.getIndex() + ":");

        final ProcessedAccelerometerData accelerometerData = accelerometerPerformanceData.getAccelerometerData();
        this.writer.println("Pitch - " + accelerometerData.getPitch());
        this.writer.println("Roll - " + accelerometerData.getRoll());
        this.writer.println("Yaw - " + accelerometerData.getYaw());

        this.writer.println(TIMESTAMP_REPRESENTATION + delay + TIMEUNIT);
        this.writer.println();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flushAndClose() {
        this.writer.flush();
        this.writer.close();
    }

    private void printIntro() {
        this.writer.println(ACCELEROMETER_DESCRIPTION);
        this.writer.println(DateHelper.toString(Instant.now()));
        this.writer.println("---");
        this.writer.println();
    }
}
