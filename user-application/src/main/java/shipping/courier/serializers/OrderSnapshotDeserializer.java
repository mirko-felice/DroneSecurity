package shipping.courier.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import shipping.courier.entities.OrderSnapshot;

import java.io.IOException;
import java.util.Date;

/**
 * Deserialize {@link OrderSnapshot} into the Java Object.
 */
public final class OrderSnapshotDeserializer extends JsonDeserializer<OrderSnapshot> {

    @Contract("_, _ -> new")
    @Override
    public @NotNull OrderSnapshot deserialize(@NotNull final JsonParser parser, final DeserializationContext ctx)
            throws IOException {
        final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        final ObjectNode root = mapper.readTree(parser);

        final SimpleModule module = new SimpleModule();
        module.addDeserializer(Date.class, new DateDeserializer());
        mapper.registerModule(module);

        final String product = root.get("product").asText();
        final Date orderDate = mapper.readValue(root.get("orderDate").toString(), Date.class);
        if (root.has("id")) {
            return new OrderSnapshot(root.get("id").asText(), product, orderDate);
        } else
            return new OrderSnapshot(product, orderDate);
    }
}
