package shipping.courier;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

/**
 * Represents the entity the Client can create which will deliver the desired product.
 */
public final class Order {

    private enum Lifecycle {
        PLACED, DELIVERING, FAILED, RESCHEDULED, DELIVERED
    }
    private static final String EMPTY_STRING = "";
    private static int fakeId = 0;
    private final transient String id;
    private final transient String product;
    private final transient Date orderDate;
    private transient Lifecycle currentState;

    /**
     * Place an Order to be delivered today.
     *
     * @param product the product to be delivered
     * @return the order to deliver today
     */
    @NotNull
    @Contract("null -> fail")
    public static Order placeToday(final String product) {
        if (product == null || product.equals(EMPTY_STRING))
            throw new IllegalArgumentException("Product MUST NOT be null or empty!");

        return new Order(product, new Date());
    }

    private Order(final String product, final Date orderDate) {
        this.id = "" + fakeId++; // TODO think about id generation
        this.product = product;
        this.orderDate = orderDate;
        this.currentState = Lifecycle.PLACED;
    }

    // STATE CHECKS
    /**
     *
     * @return true if the Order is correctly placed
     */
    public boolean isPlaced() {
        return this.currentState == Lifecycle.PLACED;
    }

    /**
     *
     * @return true if the Order is being delivered
     */
    public boolean isBeingDelivered() {
        return this.currentState == Lifecycle.DELIVERING;
    }

    /**
     *
     * @return true if the Order has been successfully delivered
     */
    public boolean hasBeenDelivered() {
        return this.currentState == Lifecycle.DELIVERED;
    }

    /**
     *
     * @return true if the Order has not been delivered
     */
    public boolean isFailed() {
        return this.currentState == Lifecycle.FAILED;
    }

    /**
     *
     * @return true if the Order is rescheduled for a new Date
     */
    public boolean isRescheduled() {
        return this.currentState == Lifecycle.RESCHEDULED;
    }

    // ACTIONS
    // TODO enforce state checks else throw exceptions
    /**
     * Deliver the Order.
     */
    public void deliver() {
        this.currentState = Lifecycle.DELIVERING;
    }

    /**
     * Confirm the Order has been successfully delivered.
     */
    public void confirmDelivery() {
        this.currentState = Lifecycle.DELIVERED;
    }

    /**
     * Miss the Order delivery.
     */
    public void missDelivery() {
        this.currentState = Lifecycle.FAILED;
    }

    /**
     * Reschedule the Order to be delivered the given @Date.
     * @param newEstimatedArrival new Date the Order will be delivered
     */
    public void rescheduleDelivery(final Date newEstimatedArrival) {
        this.currentState = Lifecycle.RESCHEDULED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id.equals(order.id) && product.equals(order.product) && orderDate.equals(order.orderDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, product, orderDate);
    }

}
