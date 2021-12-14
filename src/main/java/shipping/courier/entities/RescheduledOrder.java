package shipping.courier.entities;

/**
 * Represents an {@link Order} that has been rescheduled.
 */
public class RescheduledOrder extends PlacedOrder {

    /**
     * Construct the RescheduledOrder.
     * @param currentOrder the current state of the Order
     */
    public RescheduledOrder(final OrderSnapshot currentOrder) {
        super(currentOrder);
    }

}
