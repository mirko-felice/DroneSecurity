/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.dronesecurity.lib.Date;
import io.github.dronesecurity.lib.ValueObject;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.exceptions.InvalidDroneDataException;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.serializers.DroneDataDeserializer;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.serializers.DroneDataSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * {@link ValueObject} representing the drone data read in the instant when a {@link NegligenceReport} is being created.
 */
@JsonSerialize(using = DroneDataSerializer.class)
@JsonDeserialize(using = DroneDataDeserializer.class)
public final class DroneData implements ValueObject<DroneData> {

    private static final int MIN_ANGLE = -180;
    private static final int MAX_ANGLE = 180;
    private final Date detectionInstant;
    private final double proximity;
    private final int roll;
    private final int pitch;
    private final int yaw;
    private final long imageSize;

    private DroneData(final Date detectionInstant,
                     final double proximity,
                     final int roll,
                     final int pitch,
                     final int yaw,
                     final long imageSize) {
        this.detectionInstant = detectionInstant;
        this.validate(proximity, roll, pitch, yaw, imageSize);
        this.proximity = proximity;
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
        this.imageSize = imageSize;
    }

    /**
     * Generates a new value object using parameters.
     * @param proximity proximity distance detected
     * @param roll roll angle detected
     * @param pitch pitch angle detected
     * @param yaw yaw angle detected
     * @param imageSize size of the image detected
     * @return a new {@link DroneData}
     */
    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static @NotNull DroneData generate(final double proximity,
                                              final int roll,
                                              final int pitch,
                                              final int yaw,
                                              final long imageSize) {
        return parse(Date.now(), proximity, roll, pitch, yaw, imageSize);
    }

    /**
     * Parses the values into the value object.
     * @param detectionInstant {@link Date} of the detection
     * @param proximity proximity distance detected
     * @param roll roll angle detected
     * @param pitch pitch angle detected
     * @param yaw yaw angle detected
     * @param imageSize size of the image detected
     * @return a new {@link DroneData}
     */
    @Contract(value = "_, _, _, _, _, _ -> new", pure = true)
    public static @NotNull DroneData parse(final Date detectionInstant,
                                           final double proximity,
                                           final int roll,
                                           final int pitch,
                                           final int yaw,
                                           final long imageSize) {
        return new DroneData(detectionInstant, proximity, roll, pitch, yaw, imageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public boolean isSameValueAs(final @NotNull DroneData value) {
        return this.roll == value.roll
                && this.pitch == value.pitch
                && this.yaw == value.yaw
                && this.imageSize == value.imageSize
                && this.detectionInstant.equals(value.detectionInstant)
                && BigDecimal.valueOf(this.proximity).compareTo(BigDecimal.valueOf(value.proximity)) == 0;
    }

    /**
     * Gets the detection instant as a {@link String}.
     * @return the detection instant string representation
     */
    public @NotNull String detectionInstantAsString() {
        return this.detectionInstant.asString();
    }

    /**
     * Gets the detected proximity.
     * @return the proximity as double
     */
    public double getProximity() {
        return this.proximity;
    }

    /**
     * Gets the detected roll angle.
     * @return the roll angel as int
     */
    public int getRoll() {
        return this.roll;
    }

    /**
     * Gets the detected pitch angle.
     * @return the pitch angle as int
     */
    public int getPitch() {
        return this.pitch;
    }

    /**
     * Gets the detected yaw angle.
     * @return the yaw angle as int
     */
    public int getYaw() {
        return this.yaw;
    }

    /**
     * Gets the size of detected image.
     * @return the image size as long
     */
    public long getImageSize() {
        return this.imageSize;
    }

    private void validate(final double proximityValue,
                          final int rollAngle,
                          final int pitchAngle,
                          final int yawAngle,
                          final long imageSizeValue) {
        if (proximityValue < 0.0
                || isNotInRange(rollAngle) || isNotInRange(pitchAngle) || isNotInRange(yawAngle)
                || imageSizeValue < 0L)
            throw new InvalidDroneDataException();
    }

    private static boolean isNotInRange(final int angle) {
        return angle < MIN_ANGLE || angle > MAX_ANGLE;
    }
}
