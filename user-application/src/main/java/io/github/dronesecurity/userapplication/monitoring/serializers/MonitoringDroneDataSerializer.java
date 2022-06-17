/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.monitoring.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.dronesecurity.lib.DateHelper;
import io.github.dronesecurity.userapplication.monitoring.entities.MonitoringDroneData;
import io.github.dronesecurity.userapplication.monitoring.utilities.MonitoringConstants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Serialize {@link MonitoringDroneData} into the corresponding Json.
 */
public class MonitoringDroneDataSerializer extends JsonSerializer<MonitoringDroneData> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final @NotNull MonitoringDroneData value,
                          final @NotNull JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField(MonitoringConstants.PROXIMITY, value.getProximity());
        gen.writeObjectField(MonitoringConstants.ACCELEROMETER, value.getAccelerometer());
        gen.writeNumberField(MonitoringConstants.CAMERA, value.getCamera());
        gen.writeStringField(MonitoringConstants.DETECTION_INSTANT, DateHelper.toString(value.getDetectionInstant()));
        gen.writeNumberField(MonitoringConstants.ORDER_ID, value.getOrderId());
        gen.writeEndObject();
        gen.flush();
    }
}
