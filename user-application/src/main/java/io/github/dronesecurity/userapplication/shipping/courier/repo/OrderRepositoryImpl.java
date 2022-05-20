/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.shipping.courier.repo;

import io.github.dronesecurity.userapplication.shipping.courier.entities.*;
import io.github.dronesecurity.userapplication.utilities.VertxHelper;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
    private final SecureRandom randomGenerator = new SecureRandom();

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
        return VertxHelper.MONGO_CLIENT.find(COLLECTION_NAME, new JsonObject())
                .transform(orders -> {
                    List<Order> returningOrders;
                    if (orders.result().isEmpty()) {
                        fakeOrders.forEach(order ->
                                VertxHelper.MONGO_CLIENT.save(COLLECTION_NAME, JsonObject.mapFrom(order)));
                        returningOrders = fakeOrders;
                    } else {
                        returningOrders = orders.result().stream()
                                .map(o -> Json.decodeValue(o.toString(), Order.class))
                                .collect(Collectors.toList());
                    }
                    return Future.succeededFuture(returningOrders);
                })
                .onFailure(event -> fakeOrders.forEach(order ->
                        VertxHelper.MONGO_CLIENT.save(COLLECTION_NAME, JsonObject.mapFrom(order))));
    }

    @Override
    public Future<Order> getOrderById(final String orderId) {
        return VertxHelper.MONGO_CLIENT.findOne(COLLECTION_NAME, new JsonObject().put("id", orderId), null)
                .map(o -> Json.decodeValue(o.toString(), Order.class))
                .otherwiseEmpty();
    }

    @Override
    public void delivering(final @NotNull DeliveringOrder order) {
        this.updateOrderEvents(order);
    }

    @Override
    public void confirmedDelivery(final DeliveredOrder order) {
        this.updateOrderEvents(order);
    }

    @Override
    public void failedDelivery(final FailedOrder order) {
        this.updateOrderEvents(order);
    }

    private void updateOrderEvents(final @NotNull Order order) {
        final JsonObject query = new JsonObject();
        query.put("id", order.getId());
        final JsonObject update = new JsonObject();
        final JsonObject what = new JsonObject();
        what.put("events", order.getCurrentState());
        update.put("$push", what);
        VertxHelper.MONGO_CLIENT.updateCollection(COLLECTION_NAME, query, update);
    }

    // TODO delete it when using real orders
    private @NotNull List<Order> generateFakeOrders() {
        final List<Order> orders = new ArrayList<>();
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        for (int i = 0; i <= FAKE_PRODUCTS_MAX_VALUE; i++) {
            final int j = i;
            executor.schedule(() -> {
                final String product = FAKE_PRODUCTS[this.randomGenerator.nextInt(FAKE_PRODUCTS_MAX_VALUE)];
                orders.add(this.generateOrder(j, product));
            }, 1, TimeUnit.MILLISECONDS);
        }
        executor.shutdown();
        return orders;
    }

    @Contract("_, _ -> new")
    private @NotNull Order generateOrder(final int i, final String product) {
        final Instant now = Instant.now();
        return new PlacedOrder(String.valueOf(i), product, now, now.plus(1, ChronoUnit.DAYS));
    }
}
