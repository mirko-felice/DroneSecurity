package it.unibo.dronesecurity.userapplication.issue.courier.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.Issue;
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
        gen.writeStringField("details", value.getDetails());
    }
}
