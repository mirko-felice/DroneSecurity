package it.unibo.dronesecurity.userapplication.auth.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.auth.entities.LoggedUser;
import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.auth.entities.Role;
import it.unibo.dronesecurity.userapplication.auth.utilities.UserConstants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Deserialize Json into {@link LoggedUser}.
 */
public class LoggedUserDeserializer extends JsonDeserializer<LoggedUser> {

    /**
     * {@inheritDoc}
     */
    @Override
    public LoggedUser deserialize(final @NotNull JsonParser parser, final DeserializationContext ctxt)
            throws IOException {
        final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        final ObjectNode root = mapper.readTree(parser);
        final Role role = Role.valueOf(root.get(UserConstants.ROLE).asText());
        final String username = root.get(UserConstants.USERNAME).asText();
        switch (role) {
            case MAINTAINER:
                return new Maintainer(username);
            case COURIER:
                return new Courier(username, root.get(UserConstants.SUPERVISOR).asText());
            default:
                throw new IllegalStateException("Unexpected value: " + role);
        }
    }
}
