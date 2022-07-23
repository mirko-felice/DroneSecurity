/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.application.camera;

import io.github.dronesecurity.dronesystem.performance.domain.camera.objects.CameraPerformanceData;
import io.github.dronesecurity.dronesystem.performance.domain.camera.services.CameraOutputHelper;
import io.github.dronesecurity.lib.utilities.DateHelper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.time.Instant;

/**
 * Implementation of the {@link CameraOutputHelper}.
 */
public class CameraOutputHelperImpl implements CameraOutputHelper {

    private static final String SEP = FileSystems.getDefault().getSeparator();
    private static final String RESULT_FOLDER = "performance";
    private static final String CAMERA_PERFORMANCE_READER_FILE_NAME =
            RESULT_FOLDER + SEP + "camera_reader.txt";

    private static final String CAMERA_PERFORMANCE = "Proximity performance";
    private static final String TIMESTAMP_REPRESENTATION = "Delay - ";
    private static final String TIMEUNIT = " ms";
    private static final String PACKET_NUMBER_TEXT = "Metadata for packet #";

    private final PrintWriter writer;

    /**
     * Builds the output helper.
     * @throws IOException Whether the writer cannot be created
     */
    public CameraOutputHelperImpl() throws IOException {
        this.writer = new PrintWriter(CAMERA_PERFORMANCE_READER_FILE_NAME, StandardCharsets.UTF_8);
        this.printIntro();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printCameraPerformance(final @NotNull CameraPerformanceData cameraPerformanceData, final long delay) {
        this.writer.println(PACKET_NUMBER_TEXT + cameraPerformanceData.getIndex() + ":");
        this.writer.println("Image size - " + cameraPerformanceData.getCameraData().getImageLength());
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
        this.writer.println(CAMERA_PERFORMANCE);
        this.writer.println(DateHelper.toString(Instant.now()));
        this.writer.println("---");
        this.writer.println();
    }
}
