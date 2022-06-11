/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone;

import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Processor of raw data into more elaborated information.
 */
public class DataProcessor {

    /**
     * Elaborate raw accelerometer data to convert into useful angles.
     * @param accelerometerData {@link Map} containing the x, y and z values
     * @return a new {@link Map} containing roll, pitch and yaw angles
     */
    public @NotNull Map<String, Double> processAccelerometer(
            final @NotNull Map<String, Double> accelerometerData) {
        final Map<String, Double> angles = new ConcurrentHashMap<>();
        if (!accelerometerData.isEmpty()) {
            final double x = accelerometerData.get("x");
            final double y = accelerometerData.get("y");
            final double z = accelerometerData.get("z");

            final double roll = Math.toDegrees(Math.atan2(y, z));
            angles.put(MqttMessageParameterConstants.ROLL, this.limitDecimals(roll));

            final double pitch = Math.toDegrees(Math.atan2(-x, Math.sqrt(y * y + z * z)));
            angles.put(MqttMessageParameterConstants.PITCH, this.limitDecimals(pitch));

            final double yaw = Math.toDegrees(Math.atan2(x, y));
            angles.put(MqttMessageParameterConstants.YAW, this.limitDecimals(yaw));
        }
        return angles;
    }

    private double limitDecimals(final double value) {
        return BigDecimal.valueOf(value).setScale(0, RoundingMode.HALF_EVEN).doubleValue();
    }
}
