package it.unibo.dronesecurity.userapplication.shipping.courier.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibo.dronesecurity.userapplication.shipping.courier.entities.*;
import it.unibo.dronesecurity.userapplication.utilities.DateHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.time.Instant;

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
            final JsonNode events = root.get("events");
            final String currentState = events.get(events.size() - 1).asText();
            final String id = root.get("id").asText();
            final String product = root.get("product").asText();
            final Instant placingDate = DateHelper.toInstant(root.get("placingDate").asText());
            final Instant estimatedArrival = DateHelper.toInstant(root.get("estimatedArrival").asText());
            if (currentState.contains("place"))
                return new PlacedOrder(id, product, placingDate, estimatedArrival);
            else if (currentState.contains("fail"))
                return new FailedOrder(id, product, placingDate, estimatedArrival);
            else if (currentState.contains("delivered"))
                return new DeliveredOrder(id, product, placingDate, estimatedArrival);
            else if (currentState.contains("delivering"))
                return new DeliveringOrder(id, product, placingDate, estimatedArrival);
            else if (currentState.contains("reschedule")) {
                // TODO da modificare
                final Instant newEstimatedArrival = mapper.readValue(currentState, Instant.class);
                return new RescheduledOrder(id, product, placingDate, estimatedArrival, newEstimatedArrival);
            }
        }
        return null;
    }
}
