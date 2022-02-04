package it.unibo.dronesecurity.userapplication.shipping.courier.repo;

import io.vertx.core.*;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import it.unibo.dronesecurity.userapplication.shipping.courier.entities.DeliveringOrder;
import it.unibo.dronesecurity.userapplication.shipping.courier.entities.Order;
import it.unibo.dronesecurity.userapplication.shipping.courier.entities.OrderSnapshot;
import it.unibo.dronesecurity.userapplication.shipping.courier.entities.PlacedOrder;
import org.jetbrains.annotations.NotNull;
import it.unibo.dronesecurity.userapplication.utilities.CustomLogger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Implementation of {@link OrderRepository}.
 */
public final class OrderRepositoryImpl implements OrderRepository {

    private static final String COLLECTION_NAME = "orders";
    private static final int FAKE_PRODUCTS_MAX_VALUE = 10;
    private static final String[] FAKE_PRODUCTS = {
            "HDD", "SSD", "MOUSE", "KEYBOARD", "HEADSET", "MONITOR", "WEBCAM", "CONTROLLER", "USB", "HDMI" };
    private static OrderRepositoryImpl singleton;
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
        return this.database.find(COLLECTION_NAME, new JsonObject())
                .transform(orders -> {
                    List<Order> returningOrders;
                    if (orders.result().size() == 0) {
                        fakeOrders.forEach(order ->
                                this.database.save(COLLECTION_NAME, JsonObject.mapFrom(order)));
                        returningOrders = fakeOrders;
                    } else {
                        returningOrders = orders.result().stream()
                                .map(o -> Json.decodeValue(o.toString(), Order.class))
                                .collect(Collectors.toList());
                    }
                    return Future.succeededFuture(returningOrders);
                })
                .onFailure(event -> fakeOrders.forEach(order ->
                        this.database.save(COLLECTION_NAME, JsonObject.mapFrom(order))));
    }

    @Override
    public void delivering(final @NotNull DeliveringOrder order) {
        final JsonObject query = new JsonObject();
        query.put("id", order.getSnapshot().getId());
        final JsonObject update = new JsonObject();
        final JsonObject what = new JsonObject();
        what.put("events", order.getCurrentState());
        update.put("$push", what);
        this.database.updateCollection(COLLECTION_NAME, query, update);
    }

    // TODO delete it when using real orders
    private @NotNull List<Order> generateFakeOrders() {
        final List<Order> orders = new ArrayList<>();
        final Random r = new Random();
        for (int i = 0; i <= r.nextInt(FAKE_PRODUCTS_MAX_VALUE); i++)
            try {
                final String product = FAKE_PRODUCTS[r.nextInt(FAKE_PRODUCTS_MAX_VALUE)];
                orders.add(this.generateOrder(i, product));
                Thread.sleep(1); // it will be deleted, only to differentiate dates
            } catch (InterruptedException e) {
                CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
            }
        return orders;
    }

    private Order generateOrder(final int i, final String product) {
        return new PlacedOrder(new OrderSnapshot(String.valueOf(i), product, new Date()));
    }
}
