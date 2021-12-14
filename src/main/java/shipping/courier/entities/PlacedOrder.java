package shipping.courier.entities;

/**
 * Represents an {@link Order} that is currently only placed.
 */
public class PlacedOrder extends AbstractOrder {

    /**
     * Construct the PlacedOrder.
     * @param currentOrder the current state of the Order
     */
    public PlacedOrder(final OrderSnapshot currentOrder) {
        super(currentOrder);
    }

    /**
     * Deliver the Order returning the corresponding entity.
     * @return the {@link Order} that is being delivered.
     */
    public DeliveringOrder deliver() {
        return new DeliveringOrder(this.getSnapshot());
    }

}
