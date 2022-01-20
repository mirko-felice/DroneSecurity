package shipping.courier.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Represents an {@link Order} that has been successfully delivered.
 */
@JsonDeserialize(as = DeliveredOrder.class)
public class DeliveredOrder extends AbstractOrder {

    /**
     * Construct the DeliveredOrder.
     * @param currentOrder the current state of the Order
     */
    @JsonCreator
    public DeliveredOrder(final OrderSnapshot currentOrder) {
        super(currentOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentState() {
        return "Order is delivered.";
    }
}
