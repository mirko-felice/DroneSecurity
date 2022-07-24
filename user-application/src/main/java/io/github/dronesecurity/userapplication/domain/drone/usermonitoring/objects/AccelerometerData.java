/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.dronesecurity.lib.utilities.CastHelper;
import io.github.dronesecurity.lib.shared.Date;
import io.github.dronesecurity.lib.connection.MqttMessageParameterConstants;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.infrastructure.drone.usermonitoring.serializers.AccelerometerDataSerializer;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class representing an accelerometer data reception, containing its roll, pitch and yaw angles.
 */
@JsonSerialize(using = AccelerometerDataSerializer.class)
public final class AccelerometerData extends SensorData {

    private final int pitch;
    private final int roll;
    private final int yaw;

    /**
     * Instantiates the Accelerometer data Read event.
     *
     * @param detectionInstant {@link Instant} when the detection is happened
     * @param orderId {@link Order} identifier related to data detection
     * @param pitch The pitch received from the drone
     * @param roll The roll received from the drone
     * @param yaw The yaw received from the drone
     */
    public AccelerometerData(final Date detectionInstant, final long orderId,
                             final int pitch, final int roll, final int yaw) {
        super(detectionInstant, orderId);
        this.pitch = pitch;
        this.roll = roll;
        this.yaw = yaw;
    }

    /**
     * Gets the pitch angle related to the accelerometer data read.
     *
     * @return pitch value read by the accelerometer
     */
    public int getPitch() {
        return this.pitch;
    }

    /**
     * Gets the roll angle related to the accelerometer data read.
     *
     * @return roll value read by the accelerometer
     */
    public int getRoll() {
        return this.roll;
    }

    /**
     * Gets the yaw angle related to the accelerometer data read.
     *
     * @return yaw value read by the accelerometer
     */
    public int getYaw() {
        return this.yaw;
    }

    /**
     * Gets the accelerometer processed data as a map.
     * @return the map with respective pitch/roll/yaw angles
     */
    public @NotNull Map<String, Integer> asMap() {
        final Map<String, Integer> angles = new ConcurrentHashMap<>();
        angles.put(MqttMessageParameterConstants.PITCH, this.pitch);
        angles.put(MqttMessageParameterConstants.ROLL, this.roll);
        angles.put(MqttMessageParameterConstants.YAW, this.yaw);
        return angles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSameValueAs(final @NotNull SensorData value) {
        final Optional<AccelerometerData> accelerometerData = CastHelper.safeCast(value, AccelerometerData.class);
        return accelerometerData.filter(data -> super.isSameValueAs(value)
                && this.pitch == data.getPitch()
                && this.roll == data.getRoll()
                && this.yaw == data.getYaw()
        ).isPresent();
    }
}
