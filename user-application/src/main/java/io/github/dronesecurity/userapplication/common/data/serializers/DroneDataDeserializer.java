/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.common.data.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.userapplication.common.data.entities.DroneData;
import io.github.dronesecurity.userapplication.common.data.entities.DroneDataImpl;
import io.github.dronesecurity.userapplication.common.data.utilities.DataConstants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Deserialize Json into {@link DroneData}.
 */
public class DroneDataDeserializer extends JsonDeserializer<DroneData> {

    /**
     * {@inheritDoc}
     */
    @Override
    public DroneData deserialize(final @NotNull JsonParser parser, final DeserializationContext ctx)
            throws IOException {
        final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        final ObjectNode root = mapper.readTree(parser);

        final Double proximity = root.get(DataConstants.PROXIMITY).asDouble();
        final ConcurrentHashMap<String, Integer> accelerometer =
                mapper.readValue(root.get(DataConstants.ACCELEROMETER).toString(), new MapTypeReference());
        final Long camera = root.get(DataConstants.CAMERA).asLong();

        return new DroneDataImpl(proximity, accelerometer, camera);
    }

    /**
     * {@link TypeReference} to deserialize into a {@code Map<String, Integer>}.
     */
    private static class MapTypeReference extends TypeReference<ConcurrentHashMap<String, Integer>> { }
}
