/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.drone.usermonitoring.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.SensorData;
import io.github.dronesecurity.userapplication.infrastructure.drone.usermonitoring.MonitoringConstants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Serialize {@link SensorData} into Json.
 */
public final class SensorDataSerializer extends JsonSerializer<SensorData> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final @NotNull SensorData value,
                          final @NotNull JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField(MonitoringConstants.ORDER_ID, value.getOrderId());
        gen.writeStringField(MonitoringConstants.DETECTION_INSTANT, value.getDetectionInstant().asString());
    }
}
