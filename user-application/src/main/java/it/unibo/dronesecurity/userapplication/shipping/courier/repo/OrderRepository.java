package it.unibo.dronesecurity.userapplication.shipping.courier.repo;

import io.vertx.core.Future;
import it.unibo.dronesecurity.userapplication.shipping.courier.entities.DeliveringOrder;
import it.unibo.dronesecurity.userapplication.shipping.courier.entities.Order;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Repository to perform different actions on {@link Order} entity.
 */
public interface OrderRepository {

    /**
     * Get orders.
     * @return list of orders
     */
    Future<List<Order>> getOrders();

    /**
     * Save the delivering order.
     * @param order the order to save
     */
    void delivering(DeliveringOrder order);

    /**
     * Get the instance of this repository.
     * @return the instance
     */
    @Contract(value = " -> new", pure = true)
    static @NotNull OrderRepository getInstance() {
        return OrderRepositoryImpl.getInstance();
    }
}
