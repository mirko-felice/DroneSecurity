package shipping.courier.entities;

import java.util.Date;

/**
 * Represents an {@link Order} that failed to being delivered.
 */
public class FailedOrder extends AbstractOrder {

    /**
     * Construct the FailedOrder.
     * @param currentOrder the current state of the Order
     */
    public FailedOrder(final OrderSnapshot currentOrder) {
        super(currentOrder);
    }

    /**
     * Reschedule the Order to be delivered the given @Date.
     * @param newEstimatedArrival new Date the Order will be delivered
     * @return the {@link Order} that has been rescheduled
     */
    public RescheduledOrder rescheduleDelivery(final Date newEstimatedArrival) {
        return new RescheduledOrder(getSnapshot());
    }
}
