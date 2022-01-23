package it.unibo.dronesecurity.userapplication.shipping.courier.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibo.dronesecurity.userapplication.shipping.courier.entities.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Deserialize {@link Order} into the correct implementation basing on the current state of it.
 */
public final class OrderDeserializer extends JsonDeserializer<Order> {

    @Override
    public @Nullable Order deserialize(@NotNull final JsonParser parser, final DeserializationContext ctx)
            throws IOException {
        final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        final ObjectNode root = mapper.readTree(parser);
        if (root.has("events")) {
            final String currentState = root.get("events").get(root.get("events").size() - 1).asText();
            if (currentState.contains("place"))
                return mapper.readValue(root.toString(), PlacedOrder.class);
            else if (currentState.contains("fail"))
                return mapper.readValue(root.toString(), FailedOrder.class);
            else if (currentState.contains("delivered"))
                return mapper.readValue(root.toString(), DeliveredOrder.class);
            else if (currentState.contains("delivering"))
                return mapper.readValue(root.toString(), DeliveringOrder.class);
            else if (currentState.contains("reschedule"))
                return mapper.readValue(root.toString(), RescheduledOrder.class);
        }
        return null;
    }
}
