/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.user.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Role;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.User;
import io.github.dronesecurity.userapplication.domain.user.entities.impl.CourierImpl;
import io.github.dronesecurity.userapplication.domain.user.entities.impl.MaintainerImpl;
import io.github.dronesecurity.userapplication.domain.user.objects.Username;
import io.github.dronesecurity.userapplication.infrastructure.user.UserConstants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * Deserialize Json into {@link User}.
 */
public final class UserDeserializer extends JsonDeserializer<User> {

    /**
     * {@inheritDoc}
     */
    @Contract("_, _ -> new")
    @Override
    public @NotNull User deserialize(final @NotNull JsonParser parser, final @NotNull DeserializationContext ctx)
            throws IOException {
        final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        final ObjectNode root = mapper.readTree(parser);
        final Role role = Role.valueOf(root.get(UserConstants.ROLE).asText());
        final Username username = Username.parse(root.get(UserConstants.USERNAME).asText());
        switch (role) {
            case COURIER:
                final List<String> drones =
                        mapper.readValue(root.get(UserConstants.DRONES).toString(), new ListTypeReference());
                return new CourierImpl(username, Username.parse(root.get(UserConstants.SUPERVISOR).asText()), drones);
            case MAINTAINER:
                return new MaintainerImpl(username);
            default:
                throw new IllegalStateException("Unexpected value: " + role);
        }
    }

    /**
     * {@link TypeReference} to deserialize into a {@code List<String>}.
     */
    private static class ListTypeReference extends TypeReference<List<String>> { }
}
