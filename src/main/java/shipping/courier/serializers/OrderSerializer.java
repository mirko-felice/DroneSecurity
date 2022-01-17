package shipping.courier.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.jetbrains.annotations.NotNull;
import shipping.courier.entities.Order;
import utilities.DateHelper;

import java.io.IOException;

/**
 * Serialize {@link Order} into the corresponding Json.
 */
public final class OrderSerializer extends JsonSerializer<Order> {

    @Override
    public void serialize(final Order value, final @NotNull JsonGenerator gen, final SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", value.getSnapshot().getId());
        gen.writeStringField("product", value.getSnapshot().getProduct());
        gen.writeStringField("orderDate", DateHelper.FORMATTER.format(value.getSnapshot().getOrderDate()));
        gen.writeArrayFieldStart("events");
        gen.writeString(value.getCurrentState());
        gen.writeEndArray();
        gen.writeEndObject();
        gen.flush();
        gen.close();
    }
}
