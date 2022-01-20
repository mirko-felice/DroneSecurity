package shipping.courier.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import shipping.courier.serializers.DateSerializer;
import shipping.courier.serializers.OrderSnapshotDeserializer;

import java.util.Date;

/**
 * Represents the current state of an {@link Order}.
 */
@JsonDeserialize(using = OrderSnapshotDeserializer.class)
public class OrderSnapshot {

    private final String id;
    private final String product;
    @JsonSerialize(using = DateSerializer.class)
    private final Date orderDate;

    /**
     * Build the snapshot.
     * @param id the Order unique identifier
     * @param product the product to be delivered
     * @param orderDate the {@link Date} in which the product should be/has been delivered
     */
    public OrderSnapshot(final String id, final String product, final Date orderDate) {
        this.id = id;
        this.product = product;
        this.orderDate = orderDate;
    }

    /**
     * Build the snapshot.
     * @param product the product to be delivered
     * @param orderDate the {@link Date} in which the product should be/has been delivered
     */
    public OrderSnapshot(final String product, final Date orderDate) {
        this.id = "";
        this.product = product;
        this.orderDate = orderDate;
    }

    /**
     * Returns the Order identifier.
     * @return the {@link String} representing the identifier
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns the product to be delivered.
     * @return the {@link String} representing the product
     */
    public String getProduct() {
        return this.product;
    }

    /**
     * Returns the date in which the product should be/has been delivered.
     * @return the {@link Date} in which the product should be/has been delivered
     */
    public Date getOrderDate() {
        return this.orderDate;
    }
}
