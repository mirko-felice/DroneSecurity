package it.unibo.dronesecurity.userapplication.issue.courier.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.Issue;
import it.unibo.dronesecurity.userapplication.utilities.DateHelper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Serializes the {@link Issue}.
 */
public class IssueSerializer extends JsonSerializer<Issue> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final @NotNull Issue value, final @NotNull JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeStringField(IssueStringHelper.SUBJECT, value.getSubject());
        gen.writeStringField(IssueStringHelper.DETAILS, value.getDetails());
        gen.writeStringField(IssueStringHelper.COURIER, value.getCourier());
        gen.writeStringField(IssueStringHelper.SENDING_INSTANT,
                DateHelper.FORMATTER.format(value.getReportingDate()));
        gen.writeStringField(IssueStringHelper.STATUS, value.getState());
    }
}
