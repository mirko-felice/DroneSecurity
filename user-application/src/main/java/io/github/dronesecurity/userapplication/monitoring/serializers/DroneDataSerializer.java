/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.monitoring.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.dronesecurity.lib.DateHelper;
import io.github.dronesecurity.userapplication.monitoring.entities.DroneData;
import io.github.dronesecurity.userapplication.monitoring.utilities.DataConstants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Serialize {@link DroneData} into the corresponding Json.
 */
public class DroneDataSerializer extends JsonSerializer<DroneData> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final @NotNull DroneData value,
                          final @NotNull JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField(DataConstants.PROXIMITY, value.getProximity());
        gen.writeObjectField(DataConstants.ACCELEROMETER, value.getAccelerometer());
        gen.writeNumberField(DataConstants.CAMERA, value.getCamera());
        gen.writeStringField(DataConstants.DETECTION_INSTANT, DateHelper.toString(value.getDetectionInstant()));
        gen.writeNumberField(DataConstants.ORDER_ID, value.getOrderId());
        gen.writeEndObject();
        gen.flush();
    }
}
