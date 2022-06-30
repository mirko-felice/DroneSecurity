/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.monitoring.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.lib.DateHelper;
import io.github.dronesecurity.userapplication.domain.monitoring.entities.MonitoringDroneData;
import io.github.dronesecurity.userapplication.domain.monitoring.entities.MonitoringDroneDataImpl;
import io.github.dronesecurity.userapplication.domain.monitoring.utilities.MonitoringConstants;
import io.github.dronesecurity.userapplication.common.data.entities.DroneData;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Instant;

/**
 * Deserialize Json into {@link MonitoringDroneData}.
 */
public class MonitoringDroneDataDeserializer extends JsonDeserializer<MonitoringDroneData> {

    /**
     * {@inheritDoc}
     */
    @Override
    public MonitoringDroneData deserialize(final @NotNull JsonParser parser, final DeserializationContext ctxt)
            throws IOException {
        final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        final ObjectNode root = mapper.readTree(parser);

        final DroneData droneData = mapper.readValue(root.toString(), DroneData.class);
        final Instant detectionInstant = DateHelper.toInstant(root.get(MonitoringConstants.DETECTION_INSTANT).asText());
        final long orderId = root.get(MonitoringConstants.ORDER_ID).asLong();

        return new MonitoringDroneDataImpl(
                droneData.getProximity(),
                droneData.getAccelerometer(),
                droneData.getCamera(),
                detectionInstant,
                orderId);
    }

}
