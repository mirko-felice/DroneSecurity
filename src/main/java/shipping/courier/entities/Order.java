package shipping.courier.entities;

import java.util.Date;

// TODO probably Client will be linked
/**
 * Represents the entity that the Client can create.
 */
public interface Order {

    /**
     * Returns a snapshot of the current Order.
     * @return an {@link OrderSnapshot} instance representing the current state of the Order
     */
    OrderSnapshot getSnapshot();

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
    static PlacedOrder placeToday(final String product) {
        return new PlacedOrder(new OrderSnapshot(String.valueOf(0), product, new Date()));
    }
}
