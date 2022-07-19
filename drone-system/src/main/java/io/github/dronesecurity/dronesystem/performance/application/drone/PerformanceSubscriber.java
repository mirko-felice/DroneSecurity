/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.application.drone;

import io.github.dronesecurity.dronesystem.performance.domain.averages.objects.AveragePerformanceData;

import java.io.File;
import java.io.IOException;

/**
 * Class representing the user side, thus subscribing to the topic that shares drone sensor data.
 */
public class PerformanceSubscriber {

    private final ProximitySubscriber proximitySubscriber;
    private final AccelerometerSubscriber accelerometerSubscriber;
    private final CameraSubscriber cameraSubscriber;

    /**
     * Instantiates the performance subscriber with its file writers.
     *
     * @param cameraOutputFile The file on which write tracked camera performance details
     * @param accelerometerOutputFile The file on which write tracked accelerometer performance details
     * @param proximityOutputFile The file on which write tracked proximity sensor performance details
     * @throws IOException If the specified file does not exist
     */
    public PerformanceSubscriber(final File cameraOutputFile,
                                 final File accelerometerOutputFile,
                                 final File proximityOutputFile) throws IOException {
        this.proximitySubscriber = new ProximitySubscriber(proximityOutputFile);
        this.accelerometerSubscriber = new AccelerometerSubscriber(accelerometerOutputFile);
        this.cameraSubscriber = new CameraSubscriber(cameraOutputFile);
    }

    /**
     * Subscribes to the topic that shares data read by the camera installed on the drone.
     */
    public void subscribeToDronePerformance() {
        this.cameraSubscriber.subscribeToCameraPerformance();
        this.accelerometerSubscriber.subscribeToAccelerometerPerformance();
        this.proximitySubscriber.subscribeToProximityPerformance();
    }

    /**
     * Terminates the subscriber closing the file writer.
     */
    public void stop() {
        this.cameraSubscriber.stop();
        this.accelerometerSubscriber.stop();
        this.proximitySubscriber.stop();
    }

    /**
     * Gets the averaged delays of all performance evaluations.
     * @return the map containing the averaged performance delays
     */
    public AveragePerformanceData getAveragePerformance() {
        return new AveragePerformanceData(this.proximitySubscriber.getAverageDelay(),
                this.accelerometerSubscriber.getAverageDelay(),
                this.cameraSubscriber.getAverageDelay());
    }
}
