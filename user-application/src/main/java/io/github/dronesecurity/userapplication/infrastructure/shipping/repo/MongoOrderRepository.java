/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.shipping.repo;

import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.*;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.repo.OrderRepository;
import io.github.dronesecurity.userapplication.infrastructure.MongoRepository;
import io.github.dronesecurity.userapplication.infrastructure.shipping.OrderConstants;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link OrderRepository} to work with the underlying DB.
 */
public final class MongoOrderRepository extends MongoRepository implements OrderRepository {

    private static final String COLLECTION_NAME = "orders";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Order> listOrders() {
        return this.waitFutureResult(
                this.mongo().find(COLLECTION_NAME, new JsonObject())
                        .map(orders -> orders.stream()
                                .map(o -> Json.decodeValue(o.toString(), Order.class))
                                .collect(Collectors.toList())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Order retrieveOrderById(final @NotNull OrderIdentifier orderId) {
        return this.waitFutureResult(
                this.mongo().findOne(COLLECTION_NAME,
                                new JsonObject().put(OrderConstants.ID, orderId.asLong()), null)
                        .map(o -> Json.decodeValue(o.toString(), Order.class))
                        .otherwiseEmpty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderIdentifier nextOrderIdentifier() {
        return this.waitFutureResult(
                this.mongo().getCollections().compose(collections -> {
                    if (collections.contains(COLLECTION_NAME))
                        return this.mongo().count(COLLECTION_NAME, new JsonObject()).map(OrderIdentifier::fromLong);
                    else
                        return this.mongo().createCollection(COLLECTION_NAME)
                                .compose(unused -> Future.succeededFuture(OrderIdentifier.first()));
                }));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void placed(final PlacedOrder order) {
        this.waitFutureResult(this.mongo().save(COLLECTION_NAME, JsonObject.mapFrom(order)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delivering(final DeliveringOrder order) {
        this.waitFutureResult(this.updateOrderState(order));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void succeeded(final SucceededOrder order) {
        this.waitFutureResult(this.updateOrderState(order));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void failed(final FailedOrder order) {
        this.waitFutureResult(this.updateOrderState(order));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rescheduled(final RescheduledOrder order) {
        this.waitFutureResult(this.updateOrderState(order));
    }

    private Future<Void> updateOrderState(final @NotNull Order order) {
        final JsonObject query = new JsonObject();
        query.put(OrderConstants.ID, order.getId());
        final JsonObject update = new JsonObject();
        final JsonObject what = new JsonObject();
        what.put(OrderConstants.EVENTS, order.getCurrentState());
        update.put("$push", what);
        if (order instanceof RescheduledOrder)
            update.put("$set", new JsonObject().put(OrderConstants.NEW_ESTIMATED_ARRIVAL,
                    ((RescheduledOrder) order).getNewEstimatedArrival().asString()));
        return this.mongo().findOneAndUpdate(COLLECTION_NAME, query, update).mapEmpty();
    }
}
