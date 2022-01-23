package it.unibo.dronesecurity.userapplication.shipping.courier.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.jetbrains.annotations.NotNull;
import it.unibo.dronesecurity.userapplication.utilities.DateHelper;

import java.io.IOException;
import java.util.Date;

/**
 * Serialize Dates into String objects.
 */
public final class DateSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(final Date value, @NotNull final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException {
        gen.writeString(DateHelper.FORMATTER.format(value));
    }
}
