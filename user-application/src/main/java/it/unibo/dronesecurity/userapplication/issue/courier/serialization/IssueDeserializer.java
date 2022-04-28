package it.unibo.dronesecurity.userapplication.issue.courier.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.CreatedIssue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.Issue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.NotCreatedIssue;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Deserialize the {@link Issue}.
 */
public class IssueDeserializer extends JsonDeserializer<Issue> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Issue deserialize(final @NotNull JsonParser parser, final DeserializationContext ctxt)
            throws IOException {
        final ObjectNode root = parser.getCodec().readTree(parser);
        final String details = root.get("details").asText();
        if (root.has("ID")) {
            final int id = root.get("ID").asInt();
            return new CreatedIssue(id, details);
        }
        return new NotCreatedIssue(details);
    }
}
