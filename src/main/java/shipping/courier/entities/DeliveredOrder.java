package shipping.courier.entities;

/**
 * Represents an {@link Order} that has been successfully delivered.
 */
public class DeliveredOrder extends AbstractOrder {

    /**
     * Construct the DeliveredOrder.
     * @param currentOrder the current state of the Order
     */
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
