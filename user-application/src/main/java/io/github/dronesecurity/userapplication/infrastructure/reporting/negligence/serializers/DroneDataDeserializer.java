/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.lib.shared.Date;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.DroneData;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.NegligenceConstants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Deserialize Json into {@link DroneData}.
 */
public final class DroneDataDeserializer extends JsonDeserializer<DroneData> {

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull DroneData deserialize(final @NotNull JsonParser parser, final DeserializationContext ctxt)
            throws IOException {
        final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        final ObjectNode root = mapper.readTree(parser);
        final double proximity = root.get(NegligenceConstants.PROXIMITY).asDouble();
        final int roll;
        final int pitch;
        final int yaw;
        if (root.has(NegligenceConstants.ACCELEROMETER)) {
            final JsonNode accelerometer = root.get(NegligenceConstants.ACCELEROMETER);
            roll = accelerometer.get(NegligenceConstants.ROLL).asInt();
            pitch = accelerometer.get(NegligenceConstants.PITCH).asInt();
            yaw = accelerometer.get(NegligenceConstants.YAW).asInt();
        } else {
            roll = root.get(NegligenceConstants.ROLL).asInt();
            pitch = root.get(NegligenceConstants.PITCH).asInt();
            yaw = root.get(NegligenceConstants.YAW).asInt();
        }
        final long imageSize = root.get(NegligenceConstants.CAMERA).asLong();
        if (root.has(NegligenceConstants.DETECTION_INSTANT)) {
            final Date detectionInstant =
                    Date.parseString(root.get(NegligenceConstants.DETECTION_INSTANT).asText());
            return DroneData.parse(detectionInstant, proximity, roll, pitch, yaw, imageSize);
        } else
            return DroneData.generate(proximity, roll, pitch, yaw, imageSize);
    }
}
