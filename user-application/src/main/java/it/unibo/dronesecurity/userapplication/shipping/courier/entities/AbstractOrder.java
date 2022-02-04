package it.unibo.dronesecurity.userapplication.shipping.courier.entities;

import it.unibo.dronesecurity.userapplication.shipping.courier.specifications.NoEmptyProduct;
import it.unibo.dronesecurity.userapplication.utilities.DateHelper;

import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Abstract class to construct a generic {@link Order}.
 */
public abstract class AbstractOrder implements Order {

    private final transient String id;
    private final transient Date orderDate;
    private final transient String product;

    /**
     * Construct a generic Order.
     * @param currentOrder the current state of a generic Order
     */
    protected AbstractOrder(final OrderSnapshot currentOrder) {
        if (!new NoEmptyProduct().isSatisfiedBy(currentOrder))
            throw new IllegalArgumentException("Product MUST NOT be null or empty!");

        this.product = currentOrder.getProduct();
        this.id = currentOrder.getId(); // TODO think about new id generation (should get last id)
        this.orderDate = currentOrder.getOrderDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderSnapshot getSnapshot() {
        return new OrderSnapshot(this.id, this.product, this.orderDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", "Order" + "[", "]")
                .add("id='" + this.id + "'")
                .add("orderDate=" + DateHelper.FORMATTER.format(this.orderDate))
                .add("product='" + this.product + "'")
                .add(getCurrentState())
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AbstractOrder that = (AbstractOrder) o;
        return this.orderDate.equals(that.orderDate) && this.id.equals(that.id) && this.product.equals(that.product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.orderDate, this.product);
    }
}
