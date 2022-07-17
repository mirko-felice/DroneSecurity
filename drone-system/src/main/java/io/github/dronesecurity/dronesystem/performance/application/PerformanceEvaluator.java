/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.application;

import io.github.dronesecurity.dronesystem.performance.application.drone.DroneServiceSimulator;
import io.github.dronesecurity.dronesystem.performance.application.drone.PerformanceSubscriber;
import io.github.dronesecurity.dronesystem.performance.application.drone.PerformanceOutputHelper;
import io.github.dronesecurity.lib.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
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
    private static final String ACCELEROMENTER_PERFORMANCE_SUBSCRIBER_FILE_NAME =
            RESULT_FOLDER + SEP + "accelerometer_subscriber.txt";
    private static final String PROXIMITY_PERFORMANCE_SUBSCRIBER_FILE_NAME =
            RESULT_FOLDER + SEP + "proximity_subscriber.txt";

    private static final String AVERAGE_PERFORMANCE_RESULTS_FILE_NAME =
            RESULT_FOLDER + SEP + "average_results.txt";


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
        final File accelerometerSubscriberOutputFile = new File(ACCELEROMENTER_PERFORMANCE_SUBSCRIBER_FILE_NAME);
        final File proximitySubscriberOutputFile = new File(PROXIMITY_PERFORMANCE_SUBSCRIBER_FILE_NAME);
        final File averageResultsOutputFile = new File(AVERAGE_PERFORMANCE_RESULTS_FILE_NAME);

        try {
            final DroneServiceSimulator service = new DroneServiceSimulator();

            final PerformanceSubscriber subscriber =
                    new PerformanceSubscriber(cameraSubscriberOutputFile,
                            accelerometerSubscriberOutputFile,
                            proximitySubscriberOutputFile);

            subscriber.subscribeToDronePerformance();
            service.startDrone();

            final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(() -> {
                service.stopDrone();
                subscriber.stop();
                Connection.getInstance().closeConnection();

                try (PrintWriter averageResultsWriter =
                             new PrintWriter(averageResultsOutputFile, StandardCharsets.UTF_8)) {
                    PerformanceOutputHelper.printAverageResults(averageResultsWriter,
                            service.getAveragePerformance(),
                            subscriber.getAveragePerformance());
                } catch (IOException e) {
                    logger.error("Can NOT open files correctly.", e);
                }

                executor.shutdownNow();
            }, DELAY_SECONDS, TimeUnit.SECONDS);
        } catch (IOException e) {
            logger.error("Can NOT open files correctly.", e);
        }
    }
}
