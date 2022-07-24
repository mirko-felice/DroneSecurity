/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.drone.usermonitoring.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.lib.shared.Date;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.AccelerometerData;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.CameraData;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.ProximityData;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.SensorData;
import io.github.dronesecurity.userapplication.infrastructure.drone.usermonitoring.MonitoringConstants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Deserialize Json into {@link SensorData}.
 */
public final class SensorDataDeserializer extends JsonDeserializer<SensorData> {

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull SensorData deserialize(final @NotNull JsonParser parser, final DeserializationContext ctxt)
            throws IOException {
        final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        final ObjectNode root = mapper.readTree(parser);
        final long orderId = root.get(MonitoringConstants.ORDER_ID).asLong();
        final Date detectionInstant = Date.parseString(root.get(MonitoringConstants.DETECTION_INSTANT).asText());
        if (root.has(MonitoringConstants.PROXIMITY))
            return new ProximityData(detectionInstant, orderId, root.get(MonitoringConstants.PROXIMITY).asDouble());
        else if (root.has(MonitoringConstants.CAMERA))
            return new CameraData(detectionInstant, orderId, root.get(MonitoringConstants.CAMERA).asInt());
        else {
            final int pitch = root.get(MonitoringConstants.PITCH).asInt();
            final int roll = root.get(MonitoringConstants.ROLL).asInt();
            final int yaw = root.get(MonitoringConstants.YAW).asInt();
            return new AccelerometerData(detectionInstant, orderId, pitch, roll, yaw);
        }
    }
}
