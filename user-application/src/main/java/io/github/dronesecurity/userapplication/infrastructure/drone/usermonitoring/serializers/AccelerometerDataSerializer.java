/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.drone.usermonitoring.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.AccelerometerData;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.SensorData;
import io.github.dronesecurity.userapplication.infrastructure.drone.usermonitoring.MonitoringConstants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Serialize {@link AccelerometerData} into Json.
 */
public final class AccelerometerDataSerializer extends JsonSerializer<AccelerometerData> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final @NotNull AccelerometerData value,
                          final @NotNull JsonGenerator gen,
                          final @NotNull SerializerProvider serializers) throws IOException {
        serializers.findValueSerializer(SensorData.class).serialize(value, gen, serializers);
        gen.writeNumberField(MonitoringConstants.ROLL, value.getRoll());
        gen.writeNumberField(MonitoringConstants.PITCH, value.getPitch());
        gen.writeNumberField(MonitoringConstants.YAW, value.getYaw());
        gen.writeEndObject();
        gen.flush();
    }
}
