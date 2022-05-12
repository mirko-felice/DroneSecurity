package it.unibo.dronesecurity.userapplication.shipping.courier.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import it.unibo.dronesecurity.userapplication.shipping.courier.entities.Order;
import it.unibo.dronesecurity.userapplication.utilities.DateHelper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Serialize {@link Order} into the corresponding Json.
 */
public final class OrderSerializer extends JsonSerializer<Order> {

    @Override
    public void serialize(final @NotNull Order value, final @NotNull JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeStringField("product", value.getProduct());
        gen.writeStringField("placingDate", DateHelper.toString(value.getPlacingDate()));
        gen.writeStringField("estimatedArrival", DateHelper.toString(value.getEstimatedArrival()));
        gen.writeArrayFieldStart("events");
        gen.writeString(value.getCurrentState());
        gen.writeEndArray();
        gen.writeEndObject();
        gen.flush();
    }
}
