package shipping.courier.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Represents an {@link Order} that has been rescheduled.
 */
@JsonDeserialize(as = RescheduledOrder.class)
public class RescheduledOrder extends PlacedOrder {

    /**
     * Construct the RescheduledOrder.
     * @param currentOrder the current state of the Order
     */
    @JsonCreator
    public RescheduledOrder(@JsonProperty("snapshot") final OrderSnapshot currentOrder) {
        super(currentOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentState() {
        return "Order is rescheduled.";
    }
}
