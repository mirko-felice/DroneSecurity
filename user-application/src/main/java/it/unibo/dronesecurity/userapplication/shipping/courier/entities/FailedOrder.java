package it.unibo.dronesecurity.userapplication.shipping.courier.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

/**
 * Represents an {@link Order} that failed to being delivered.
 */
@JsonDeserialize(as = FailedOrder.class)
public class FailedOrder extends AbstractOrder {

    /**
     * Construct the FailedOrder.
     * @param currentOrder the current state of the Order
     */
    @JsonCreator
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentState() {
        return "Order fails delivery.";
    }
}
