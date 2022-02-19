package it.unibo.dronesecurity.userapplication.shipping.courier.entities;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Represents an {@link Order} that has been rescheduled.
 */
public final class RescheduledOrder extends PlacedOrder {

    private final transient Instant newEstimatedArrival;

    /**
     * Build the rescheduled Order.
     * @param id the order identifier
     * @param product the ordered product
     * @param placingDate the date in which the order has been placed
     * @param oldArrival the date in which the order should have arrived
     * @param newEstimatedArrival the new date in which the order should arrive
     */
    public RescheduledOrder(final String id, final String product, final Instant placingDate, final Instant oldArrival,
                            final Instant newEstimatedArrival) {
        super(id, product, placingDate, oldArrival);
        this.newEstimatedArrival = newEstimatedArrival;
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public @NotNull String getCurrentState() {
        return "Order is rescheduled.";
    }

    /**
     * Returns the new estimated arrival date.
     * @return the date in which the order should arrive
     */
    public Instant getNewEstimatedArrival() {
        return this.newEstimatedArrival;
    }
}
