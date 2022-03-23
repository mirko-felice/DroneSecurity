package it.unibo.dronesecurity.userapplication.shipping.courier.entities;

import it.unibo.dronesecurity.userapplication.utilities.DateHelper;
import it.unibo.dronesecurity.userapplication.utilities.EmptyProductException;

import java.time.Instant;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Abstract class to construct a generic {@link Order}.
 */
public abstract class AbstractOrder implements Order {

    private final String id;
    private final String product;
    private final Instant placingDate;
    private final Instant estimatedArrival;

    /**
     * Build a generic Order.
     * @param id the order identifier
     * @param product the ordered product
     * @param placingDate the date in which the order has been placed
     * @param estimatedArrival the date in which the order should arrive
     */
    protected AbstractOrder(final String id, final String product, final Instant placingDate,
                            final Instant estimatedArrival) {
        if (product == null || product.isEmpty())
            throw new EmptyProductException();

        this.id = id; // TODO think about new id generation (should get last id)
        this.product = product;
        this.placingDate = placingDate;
        this.estimatedArrival = estimatedArrival; // TODO check estimated is after placing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProduct() {
        return this.product;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getPlacingDate() {
        return this.placingDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getEstimatedArrival() {
        return this.estimatedArrival;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", "Order" + "[", "]")
                .add("id='" + this.id + "'")
                .add("placingDate=" + DateHelper.FORMATTER.format(this.placingDate))
                .add("product='" + this.product + "'")
                .add(this.getCurrentState())
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
        return this.placingDate.equals(that.placingDate) && this.id.equals(that.id)
                && this.product.equals(that.product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.placingDate, this.product);
    }
}
