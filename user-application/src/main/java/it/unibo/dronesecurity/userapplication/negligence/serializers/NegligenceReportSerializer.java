package it.unibo.dronesecurity.userapplication.negligence.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import it.unibo.dronesecurity.userapplication.negligence.entities.ClosedNegligenceReport;
import it.unibo.dronesecurity.userapplication.negligence.entities.NegligenceReport;
import it.unibo.dronesecurity.userapplication.utilities.DateHelper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Instant;

/**
 * Serialize {@link NegligenceReport} into the corresponding Json.
 */
public class NegligenceReportSerializer extends JsonSerializer<NegligenceReport> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final @NotNull NegligenceReport value, final @NotNull JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("negligent", value.getNegligent().getUsername());
        gen.writeStringField("assigner", value.getAssigner().getUsername());
        gen.writeObjectField("data", value.getData());
        if (value instanceof ClosedNegligenceReport) {
            final Instant closingInstant = ((ClosedNegligenceReport) value).getClosingInstant();
            gen.writeStringField("closingInstant", DateHelper.toString(closingInstant));
        }
        gen.writeEndObject();
        gen.flush();
    }
}
