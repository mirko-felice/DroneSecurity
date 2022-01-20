package shipping.courier.repo;

import io.vertx.core.*;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.jetbrains.annotations.NotNull;
import shipping.courier.entities.DeliveringOrder;
import shipping.courier.entities.Order;
import utilities.CustomLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Implementation of {@link OrderRepository}.
 */
public final class OrderRepositoryImpl implements OrderRepository {

    private static OrderRepositoryImpl singleton;
    private static final int FAKE_PRODUCTS_MAX_VALUE = 10;
    private static final String[] FAKE_PRODUCTS = {
            "HDD", "SSD", "MOUSE", "KEYBOARD", "HEADSET", "MONITOR", "WEBCAM", "CONTROLLER", "USB", "HDMI" };
    private final transient MongoClient database;

    private OrderRepositoryImpl() {
        final JsonObject config = new JsonObject();
        config.put("db_name", "drone");
        this.database = MongoClient.create(Vertx.currentContext().owner(), config);
    }

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
    public Future<List<Order>> getOrders() {
        final List<Order> fakeOrders = this.generateFakeOrders();
        return database.find("orders", new JsonObject())
                .map(orders -> orders.stream()
                        .map(o -> Json.decodeValue(o.toString(), Order.class))
                        .collect(Collectors.toList()))
                .otherwise(fakeOrders)
                .onFailure(event -> fakeOrders.forEach(order ->
                        database.save("orders", JsonObject.mapFrom(order))));
    }

    @Override
    public void delivering(final @NotNull DeliveringOrder order) {
        final JsonObject query = new JsonObject();
        query.put("id", order.getSnapshot().getId());
        final JsonObject update = new JsonObject();
        final JsonObject what = new JsonObject();
        what.put("events", order.getCurrentState());
        update.put("$push", what);
        database.updateCollection("orders", query, update);
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
