/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.DroneData;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.NegligenceConstants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Serialize {@link DroneData} into Json.
 */
public final class DroneDataSerializer extends JsonSerializer<DroneData> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final @NotNull DroneData value,
                          final @NotNull JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField(NegligenceConstants.DETECTION_INSTANT, value.detectionInstantAsString());
        gen.writeNumberField(NegligenceConstants.PROXIMITY, value.getProximity());
        gen.writeNumberField(NegligenceConstants.ROLL, value.getRoll());
        gen.writeNumberField(NegligenceConstants.PITCH, value.getPitch());
        gen.writeNumberField(NegligenceConstants.YAW, value.getYaw());
        gen.writeNumberField(NegligenceConstants.CAMERA, value.getImageSize());
        gen.writeEndObject();
        gen.flush();
    }
}
