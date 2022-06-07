/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.auth.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.userapplication.auth.utilities.UserConstants;
import io.github.dronesecurity.userapplication.auth.entities.Courier;
import io.github.dronesecurity.userapplication.auth.entities.LoggedUser;
import io.github.dronesecurity.userapplication.auth.entities.Maintainer;
import io.github.dronesecurity.userapplication.auth.entities.Role;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Deserialize Json into {@link LoggedUser}.
 */
public class LoggedUserDeserializer extends JsonDeserializer<LoggedUser> {

    /**
     * {@inheritDoc}
     */
    @Override
    public LoggedUser deserialize(final @NotNull JsonParser parser, final @NotNull DeserializationContext ctx)
            throws IOException {
        final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        final ObjectNode root = mapper.readTree(parser);
        final Role role = Role.valueOf(root.get(UserConstants.ROLE).asText());
        final String username = root.get(UserConstants.USERNAME).asText();
        switch (role) {
            case MAINTAINER:
                final List<String> couriers = Arrays.asList((String[]) ctx.getAttribute(UserConstants.COURIERS));
                return new Maintainer(username, couriers);
            case COURIER:
                return new Courier(username, root.get(UserConstants.SUPERVISOR).asText());
            default:
                throw new IllegalStateException("Unexpected value: " + role);
        }
    }
}
