/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone;

import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Processor of raw data into more elaborated information.
 */
public final class DataProcessor {

    private DataProcessor() { }

    /**
     * Elaborate raw accelerometer data to convert into useful angles.
     * @param accelerometerData {@link Map} containing the x, y and z values
     * @return a new {@link Map} containing roll, pitch and yaw angles
     */
    public static @NotNull Map<String, Integer> processAccelerometer(
            final @NotNull Map<String, Double> accelerometerData) {
        final Map<String, Integer> angles = new ConcurrentHashMap<>();
        if (!accelerometerData.isEmpty()) {
            final double x = accelerometerData.get(AccelerometerConstants.X);
            final double y = accelerometerData.get(AccelerometerConstants.Y);
            final double z = accelerometerData.get(AccelerometerConstants.Z);

            final double roll = Math.toDegrees(Math.atan2(y, z));
            angles.put(MqttMessageParameterConstants.ROLL, (int) roll);

            final double pitch = Math.toDegrees(Math.atan2(-x, Math.sqrt(y * y + z * z)));
            angles.put(MqttMessageParameterConstants.PITCH, (int) pitch);

            final double yaw = Math.toDegrees(Math.atan2(x, y));
            angles.put(MqttMessageParameterConstants.YAW, (int) yaw);
        }
        return angles;
    }
}
