package it.unibo.dronesecurity.userapplication.shipping.courier.entities;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Represents an {@link Order} that is currently being delivered.
 */
public final class DeliveringOrder extends AbstractOrder {

    /**
     * Build the delivering Order.
     * @param id the order identifier
     * @param product the ordered product
     * @param placingDate the date in which the order has been placed
     * @param estimatedArrival the date in which the order should arrive
     */
    public DeliveringOrder(final String id, final String product, final Instant placingDate,
                           final Instant estimatedArrival) {
        super(id, product, placingDate, estimatedArrival);
    }

    /**
     * Confirm the delivery.
     * @return the {@link Order} successfully delivered.
     */
    @Contract(" -> new")
    public @NotNull DeliveredOrder confirmDelivery() {
        return new DeliveredOrder(this.getId(), this.getProduct(), this.getPlacingDate(), this.getEstimatedArrival());
    }

    /**
     * Miss the delivery.
     * @return the {@link Order} representing the delivery fail.
     */
    @Contract(" -> new")
    public @NotNull FailedOrder failDelivery() {
        return new FailedOrder(this.getId(), this.getProduct(), this.getPlacingDate(), this.getEstimatedArrival());
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public @NotNull String getCurrentState() {
        return "Order is delivering.";
    }
}
