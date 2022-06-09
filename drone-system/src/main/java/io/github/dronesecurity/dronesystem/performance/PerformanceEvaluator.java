/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance;

import io.github.dronesecurity.lib.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class performing performance evaluation on the drone sensor's readings,
 * tracking basically the latency between sensor readings and the delivery of the data to the application.
 */
public final class PerformanceEvaluator {

    private static final int DELAY_SECONDS = 10;
    private static final String SEP = FileSystems.getDefault().getSeparator();
    private static final String RESULT_FOLDER = "performance";
    private static final String CAMERA_PERFORMANCE_SUBSCRIBER_FILE_NAME =
            RESULT_FOLDER + SEP + "camera_subscriber.txt";
    private static final String CAMERA_PERFORMANCE_READER_FILE_NAME =
            RESULT_FOLDER + SEP + "camera_reader.txt";
    private static final String ACCELEROMENTER_PERFORMANCE_SUBSCRIBER_FILE_NAME =
            RESULT_FOLDER + SEP + "accelerometer_subscriber.txt";
    private static final String ACCELEROMENTER_PERFORMANCE_READER_FILE_NAME =
            RESULT_FOLDER + SEP + "accelerometer_reader.txt";
    private static final String ACCELEROMENTER_DATA_PROCESSING_FILE_NAME =
            RESULT_FOLDER + SEP + "accelerometer_processing.txt";

    private PerformanceEvaluator() { }

    /**
     * Main method executing the performance evaluation.
     * @param args arguments to be passed to the application (none is needed for this one)
     */
    public static void main(final String[] args) {
        Connection.getInstance().connect();
        final Logger logger = LoggerFactory.getLogger(PerformanceEvaluator.class);
        try {
            final Path resultFolderPath = Path.of(RESULT_FOLDER);
            if (Files.notExists(resultFolderPath))
                Files.createDirectory(resultFolderPath);
        } catch (IOException e) {
            logger.error("Couldn't create result folder correctly.", e);
        }
        final File cameraSubscriberOutputFile = new File(CAMERA_PERFORMANCE_SUBSCRIBER_FILE_NAME);
        final File cameraReaderOutputFile = new File(CAMERA_PERFORMANCE_READER_FILE_NAME);
        final File accelerometerSubscriberOutputFile = new File(ACCELEROMENTER_PERFORMANCE_SUBSCRIBER_FILE_NAME);
        final File accelerometerReaderOutputFile = new File(ACCELEROMENTER_PERFORMANCE_READER_FILE_NAME);
        final File accelerometerProcessingOutputFile = new File(ACCELEROMENTER_DATA_PROCESSING_FILE_NAME);
        try {
            final DroneServiceSimulator service =
                    new DroneServiceSimulator(cameraReaderOutputFile,
                            accelerometerReaderOutputFile,
                            accelerometerProcessingOutputFile);
            final PerformanceSubscriber subscriber =
                    new PerformanceSubscriber(cameraSubscriberOutputFile,
                            accelerometerSubscriberOutputFile);
            service.startDrone();
            subscriber.subscribeToCameraPerformance();
            final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(() -> {
                service.stopDrone();
                subscriber.stop();
                Connection.getInstance().closeConnection();
                executor.shutdownNow();
            }, DELAY_SECONDS, TimeUnit.SECONDS);
        } catch (IOException e) {
            logger.error("Can NOT open files correctly.", e);
        }
    }
}
