/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.sensor;

import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.exceptions.NotAcceptableAngleException;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.ProcessedAccelerometerData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.exceptions.NotAcceptableImageSizeException;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.ProcessedCameraData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.RawCameraData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.exceptions.NotAcceptableProximityException;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.ProcessedProximityData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.RawProximityData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing sensor data creation correctness.
 */
class SensorDataTest {

    private static final double VALID_DISTANCE = 15.0;
    private static final double INVALID_DISTANCE = -15.0;

    private static final int VALID_ANGLE = 0;
    private static final int INVALID_ANGLE = -200;

    private static final int VALID_IMAGE_SIZE = 1_000;
    private static final int INVALID_IMAGE_SIZE = -10;

    /**
     * Tests whether proximity data are correctly checked before instantiating them.
     */
    @Test
    void proximityValuesTest() {
        Assertions.assertDoesNotThrow(() -> new RawProximityData(VALID_DISTANCE));
        Assertions.assertThrows(NotAcceptableProximityException.class, () -> new RawProximityData(INVALID_DISTANCE));

        Assertions.assertDoesNotThrow(() -> new ProcessedProximityData(VALID_DISTANCE));
        Assertions.assertThrows(NotAcceptableProximityException.class,
                () -> new ProcessedProximityData(INVALID_DISTANCE));
    }

    /**
     * Tests whether accelerometer data are correctly checked before instantiating them.
     */
    @Test
    void accelerometerValuesTest() {
        Assertions.assertDoesNotThrow(() -> new ProcessedAccelerometerData(VALID_ANGLE, VALID_ANGLE, VALID_ANGLE));

        Assertions.assertThrows(NotAcceptableAngleException.class,
                () -> new ProcessedAccelerometerData(INVALID_ANGLE, VALID_ANGLE, VALID_ANGLE));

        Assertions.assertThrows(NotAcceptableAngleException.class,
                () -> new ProcessedAccelerometerData(VALID_ANGLE, INVALID_ANGLE, VALID_ANGLE));

        Assertions.assertThrows(NotAcceptableAngleException.class,
                () -> new ProcessedAccelerometerData(VALID_ANGLE, VALID_ANGLE, INVALID_ANGLE));
    }

    /**
     * Tests whether camera data are correctly checked before instantiating them.
     */
    @Test
    void cameraValuesTest() {
        Assertions.assertDoesNotThrow(() -> new RawCameraData(VALID_IMAGE_SIZE));
        Assertions.assertThrows(NotAcceptableImageSizeException.class,
                () -> new RawCameraData(INVALID_IMAGE_SIZE));

        Assertions.assertDoesNotThrow(() -> new ProcessedCameraData(VALID_ANGLE));
        Assertions.assertThrows(NotAcceptableImageSizeException.class,
                () -> new ProcessedCameraData(INVALID_IMAGE_SIZE));
    }
}
