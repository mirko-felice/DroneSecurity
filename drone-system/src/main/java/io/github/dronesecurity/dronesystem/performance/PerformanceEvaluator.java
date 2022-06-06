/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance;

import io.github.dronesecurity.lib.Connection;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class performing performance evaluation on the drone sensor's readings,
 * tracking basically the latency between sensor readings and the delivery of the data to the application.
 */
public final class PerformanceEvaluator {

    private static final int DELAY_SECONDS = 10;
    private static final String CAMERA_PERFORMANCE_SUBSCRIBER_FILE_NAME = "camera_subscriber.txt";
    private static final String CAMERA_PERFORMANCE_READER_FILE_NAME = "camera_reader.txt";

    private PerformanceEvaluator() { }

    /**
     * Main method executing the performance evaluation.
     * @param args arguments to be passed to the application (none is needed for this one)
     */
    public static void main(final String[] args) {
        Connection.getInstance().connect();
        final File cameraSubscriberOutputFile = new File(CAMERA_PERFORMANCE_SUBSCRIBER_FILE_NAME);
        final File cameraReaderOutputFile = new File(CAMERA_PERFORMANCE_READER_FILE_NAME);
        try {
            final DroneServiceSimulator service = new DroneServiceSimulator(cameraReaderOutputFile);
            final PerformanceSubscriber subscriber = new PerformanceSubscriber(cameraSubscriberOutputFile);
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
            LoggerFactory.getLogger(PerformanceEvaluator.class).error("Can NOT open files correctly.", e);
        }
    }
}
