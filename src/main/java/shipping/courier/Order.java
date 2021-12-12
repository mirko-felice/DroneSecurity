package shipping.courier;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

/**
 * Represents the entity the Client can create which will deliver the desired product.
 */
public final class Order {

    private static final String EMPTY_STRING = "";
    private static int fakeId = 0;
    private final transient String id;
    private final transient String product;
    private final transient Date orderDate;

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
