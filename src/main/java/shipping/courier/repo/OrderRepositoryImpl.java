package shipping.courier.repo;

import org.jetbrains.annotations.NotNull;
import shipping.courier.entities.Order;
import utilities.CustomLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of {@link OrderRepository}.
 */
public final class OrderRepositoryImpl implements OrderRepository {

    private static OrderRepositoryImpl singleton;
    private static final int FAKE_PRODUCTS_MAX_VALUE = 10;
    private static final String[] FAKE_PRODUCTS = {
            "HDD", "SSD", "MOUSE", "KEYBOARD", "HEADSET", "MONITOR", "WEBCAM", "CONTROLLER", "USB", "HDMI" };

    /**
     * Get the Singleton instance.
     * @return the singleton
     */
    public static OrderRepositoryImpl getInstance() {
        synchronized (OrderRepositoryImpl.class) {
            if (singleton == null)
                singleton = new OrderRepositoryImpl();
            return singleton;
        }
    }

    @Override
    public List<Order> getOrders() {
        return this.generateFakeOrders();
    }

    // TODO delete it when using real orders
    private @NotNull List<Order> generateFakeOrders() {
        final List<Order> orders = new ArrayList<>();
        final Random r = new Random();
        for (int i = 0; i <= r.nextInt(FAKE_PRODUCTS_MAX_VALUE); i++)
            try {
                orders.add(Order.placeToday(FAKE_PRODUCTS[r.nextInt(FAKE_PRODUCTS_MAX_VALUE)]));
                Thread.sleep(1); // it will be deleted, only to differentiate dates
            } catch (InterruptedException e) {
                CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
            }
        return orders;
    }
}
