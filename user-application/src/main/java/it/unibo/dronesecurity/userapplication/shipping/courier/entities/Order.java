package it.unibo.dronesecurity.userapplication.shipping.courier.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.unibo.dronesecurity.userapplication.shipping.courier.serializers.OrderDeserializer;
import it.unibo.dronesecurity.userapplication.shipping.courier.serializers.OrderSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

// TODO probably Client will be linked
/**
 * Represents the entity that the Client can create.
 */
@JsonDeserialize(using = OrderDeserializer.class)
@JsonSerialize(using = OrderSerializer.class)
public interface Order {

    /**
     * Returns the order identifier.
     * @return the order ID
     */
    String getId();

    /**
     * Returns the ordered product.
     * @return the product
     */
    String getProduct();

    /**
     * Returns the date in which the order has been placed.
     * @return the placing date
     */
    Instant getPlacingDate();

    /**
     * Returns the date in which the order should arrive.
     * @return the estimated arrival
     */
    Instant getEstimatedArrival();

    /**
     * Get the current state of the Order.
     * @return the String representation of current state
     */
    String getCurrentState();

    /**
     * Place an Order to be delivered today.
     *
     * @param product the product to be delivered
     * @return the {@link Order} to deliver today
     */
    @Contract("_ -> new")
    static @NotNull PlacedOrder placeToday(final String product) {
        final Instant now = Instant.now();
        return new PlacedOrder(String.valueOf(0), product,  now, now.plus(1, ChronoUnit.DAYS));
    }
}
