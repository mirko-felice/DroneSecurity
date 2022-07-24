/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.drone.usermonitoring.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.CameraData;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.SensorData;
import io.github.dronesecurity.userapplication.infrastructure.drone.usermonitoring.MonitoringConstants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Serialize {@link CameraData} into Json.
 */
public final class CameraDataSerializer extends JsonSerializer<CameraData> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final @NotNull CameraData value,
                          final @NotNull JsonGenerator gen,
                          final @NotNull SerializerProvider serializers) throws IOException {
        serializers.findValueSerializer(SensorData.class).serialize(value, gen, serializers);
        gen.writeNumberField(MonitoringConstants.CAMERA, value.getImageSize());
        gen.writeEndObject();
        gen.flush();
    }
}
