package shipping.courier.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import shipping.courier.serializers.DateDeserializer;

import java.util.Date;

/**
 * Represents the current state of an {@link Order}.
 */
public class OrderSnapshot {

    private final transient String id;
    private final String product;
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
    @JsonCreator
    public OrderSnapshot(
            @JsonProperty("product") final String product,
            @JsonProperty("orderDate") @JsonDeserialize(using = DateDeserializer.class) final Date orderDate) {
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
