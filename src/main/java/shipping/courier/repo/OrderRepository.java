package shipping.courier.repo;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import shipping.courier.entities.Order;

import java.util.List;

/**
 * Repository to perform different actions on {@link Order} entity.
 */
public interface OrderRepository {

    /**
     * Get orders.
     * @return list of orders
     */
    List<Order> getOrders();

    /**
     * Get the instance of this repository.
     * @return the instance
     */
    @Contract(value = " -> new", pure = true)
    static @NotNull OrderRepository getInstance() {
        return OrderRepositoryImpl.getInstance();
    }
}
