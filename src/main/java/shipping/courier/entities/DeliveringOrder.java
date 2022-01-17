package shipping.courier.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Represents an {@link Order} that is currently being delivered.
 */
@JsonDeserialize(as = DeliveringOrder.class)
public class DeliveringOrder extends AbstractOrder {

    /**
     * Construct the DeliveringOrder.
     * @param currentOrder the current state of the Order
     */
    @JsonCreator
    public DeliveringOrder(@JsonProperty("snapshot") final OrderSnapshot currentOrder) {
        super(currentOrder);
    }

    /**
     * Confirm the delivery.
     * @return the {@link Order} successfully delivered.
     */
    public DeliveredOrder confirmDelivery() {
        return new DeliveredOrder(this.getSnapshot());
    }

    /**
     * Miss the delivery.
     * @return the {@link Order} representing the delivery fail.
     */
    public FailedOrder failDelivery() {
        return new FailedOrder(this.getSnapshot());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentState() {
        return "Order is delivering.";
    }
}
