/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.application.drone.accelerometer;

import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.ProcessedAccelerometerData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.RawAccelerometerData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.services.AccelerometerDataProcessor;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link AccelerometerDataProcessor} that transforms it into pitch/roll/yaw angles.
 */
public class AccelerometerDataProcessorImpl implements AccelerometerDataProcessor {

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessedAccelerometerData processAccelerometerData(final @NotNull RawAccelerometerData accelerometerData) {

        final double x = accelerometerData.getX();
        final double y = accelerometerData.getY();
        final double z = accelerometerData.getZ();

        final double pitch = Math.toDegrees(Math.atan2(-x, Math.sqrt(y * y + z * z)));
        final double roll = Math.toDegrees(Math.atan2(y, z));
        final double yaw = Math.toDegrees(Math.atan2(x, y));

        return new ProcessedAccelerometerData((int) pitch, (int) roll, (int) yaw);
    }
}
