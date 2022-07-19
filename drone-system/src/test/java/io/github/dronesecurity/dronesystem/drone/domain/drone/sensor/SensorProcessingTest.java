/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.sensor;

import io.github.dronesecurity.dronesystem.drone.application.drone.accelerometer.AccelerometerDataProcessorImpl;
import io.github.dronesecurity.dronesystem.drone.application.drone.camera.CameraDataProcessorImpl;
import io.github.dronesecurity.dronesystem.drone.application.drone.proximity.ProximityDataProcessorImpl;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.ProcessedAccelerometerData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.RawAccelerometerData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.services.AccelerometerDataProcessor;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.ProcessedCameraData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.RawCameraData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.services.CameraDataProcessor;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.ProcessedProximityData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.RawProximityData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.services.ProximityDataProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for processing of raw sensor data.
 */
class SensorProcessingTest {

    private static final double TEST_DISTANCE = 15.0;
    private static final int RIGHT_ANGLE = 90;
    private static final int IMAGE_TEST_SIZE = 100;

    private static final RawAccelerometerData RAW_ACCELEROMETER_DATA = new RawAccelerometerData(0.0, 1.0, 0.0);
    private static final RawProximityData RAW_PROXIMITY_DATA = new RawProximityData(TEST_DISTANCE);
    private static final RawCameraData RAW_CAMERA_DATA = new RawCameraData(IMAGE_TEST_SIZE);

    /**
     * Tests accelerometer processor.
     */
    @Test
    void accelerometerProcessingTest() {
        final AccelerometerDataProcessor accelerometerDataProcessor = new AccelerometerDataProcessorImpl();

        final ProcessedAccelerometerData processedData =
                accelerometerDataProcessor.processAccelerometerData(RAW_ACCELEROMETER_DATA);

        Assertions.assertEquals(0, processedData.getPitch(),
                "Pitch angle, with acceleration on axis X equal to 0 and acceleration on axis Y equal to 1, "
                        + "should be 0.");
        Assertions.assertEquals(RIGHT_ANGLE, processedData.getRoll(),
                "Roll angle, with acceleration on axis X equal to 0 and acceleration on axis Y equal to 1, "
                        + "should be " + RIGHT_ANGLE + " degrees.");
    }

    /**
     * Tests proximity data processor.
     */
    @Test
    void proximityProcessingTest() {
        final ProximityDataProcessor proximityDataProcessor = new ProximityDataProcessorImpl();

        final ProcessedProximityData processedData =
                proximityDataProcessor.processProximityData(RAW_PROXIMITY_DATA);

        Assertions.assertEquals(TEST_DISTANCE, processedData.getDistance(),
                "Proximity processor should not transform initial distance.");
    }

    /**
     * Tests camera data processor.
     */
    @Test
    void cameraProcessingTest() {
        final CameraDataProcessor cameraDataProcessor = new CameraDataProcessorImpl();

        final ProcessedCameraData processedData =
                cameraDataProcessor.processCameraData(RAW_CAMERA_DATA);

        Assertions.assertEquals(IMAGE_TEST_SIZE, processedData.getImageLength(),
                "Camera processor should not transform initial image size.");
    }
}
