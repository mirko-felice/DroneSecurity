package it.unibo.dronesecurity.userapplication.issue.courier.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.OpenIssue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.Issue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.BaseIssue;
import it.unibo.dronesecurity.userapplication.utilities.DateHelper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
        final String subject = root.get("subject").asText();
        final String details = root.get("details").asText();
        final String courierUsername = root.get("courier").asText();
        final ZoneId zoneId = ZoneId.systemDefault();
        final Instant sendingInstant = LocalDateTime.parse(root.get("sent").asText(),
                DateHelper.FORMATTER).atZone(zoneId).toInstant();
        if (root.has("ID")) {
            final int id = root.get("ID").asInt();
            return new OpenIssue(subject, id, details, courierUsername, sendingInstant);
        }
        return new BaseIssue(subject, details, courierUsername, sendingInstant);
    }
}
