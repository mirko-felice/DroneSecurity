package it.unibo.dronesecurity.userapplication.negligence.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import it.unibo.dronesecurity.userapplication.negligence.entities.ClosedNegligenceReport;
import it.unibo.dronesecurity.userapplication.negligence.entities.NegligenceReport;
import it.unibo.dronesecurity.userapplication.utilities.DateHelper;
import it.unibo.dronesecurity.userapplication.negligence.utilities.NegligenceConstants;
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
        gen.writeStringField(NegligenceConstants.NEGLIGENT, value.getNegligent().getUsername());
        gen.writeStringField(NegligenceConstants.ASSIGNEE, value.assignedTo().getUsername());
        gen.writeObjectField(NegligenceConstants.DATA, value.getData());
        if (value instanceof ClosedNegligenceReport) {
            final Instant closingInstant = ((ClosedNegligenceReport) value).getClosingInstant();
            gen.writeStringField(NegligenceConstants.CLOSING_INSTANT, DateHelper.toString(closingInstant));
        }
        gen.writeEndObject();
        gen.flush();
    }
}
