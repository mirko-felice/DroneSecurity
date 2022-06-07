/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.shipping.courier;

import io.github.dronesecurity.userapplication.shipping.courier.entities.DeliveringOrder;
import io.github.dronesecurity.userapplication.shipping.courier.entities.FailedOrder;
import io.github.dronesecurity.userapplication.shipping.courier.entities.PlacedOrder;
import io.github.dronesecurity.userapplication.shipping.courier.entities.RescheduledOrder;
import io.github.dronesecurity.userapplication.shipping.courier.repo.OrderRepository;
import io.github.dronesecurity.userapplication.shipping.courier.utilities.ServiceHelper;
import io.github.dronesecurity.userapplication.utilities.CastHelper;
import io.github.dronesecurity.lib.DateHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the Service to perform operations useful to the Courier.
 */
public final class CourierShippingService extends AbstractVerticle {

    private static final String OPEN_API_URL = "https://raw.githubusercontent.com/mirko-felice/DroneSecurity/develop/"
            + "user-application/src/main/resources/io/github/dronesecurity/userapplication/shipping/courier/"
            + "courierShippingService.json";
    private static final String CORRECT_RESPONSE_TO_PERFORM_DELIVERY = "Delivery is performing...";
    private static final String CORRECT_RESPONSE_TO_RESCHEDULE_DELIVERY = "Order rescheduled.";
    private static final String DEFAULT_KEY = "default";
    private static final String SEP = "/";
    private static final int CLIENT_ERROR_CODE = 400;
    private static final OrderRepository REPOSITORY = OrderRepository.getInstance();

    @Override
    public void start(final @NotNull Promise<Void> startPromise) {
        final Router globalRouter = Router.router(this.getVertx());
        RouterBuilder.create(this.getVertx(), OPEN_API_URL)
                .onSuccess(routerBuilder -> {
                    this.setupOperations(routerBuilder);

                    final JsonArray servers = routerBuilder.getOpenAPI().getOpenAPI().getJsonArray("servers");
                    final int serversCount = servers.size();
                    final List<Future<?>> futures = new ArrayList<>(serversCount);
                    for (int i = 0; i < serversCount; i++) {
                        final JsonObject server = servers.getJsonObject(i);
                        final JsonObject variables = server.getJsonObject("variables");

                        final String basePath = SEP + variables.getJsonObject("basePath").getString(DEFAULT_KEY)
                                + SEP + "*";
                        final int port = Integer.parseInt(variables.getJsonObject("port").getString(DEFAULT_KEY));
                        final String host = variables.getJsonObject("host").getString(DEFAULT_KEY);

                        globalRouter.route(basePath).subRouter(routerBuilder.createRouter());
                        futures.add(this.getVertx().createHttpServer().requestHandler(globalRouter).listen(port, host));
                    }
                    CompositeFuture.all(Arrays.asList(futures.toArray(new Future[0])))
                            .onSuccess(ignored -> startPromise.complete());
                })
                .onFailure(startPromise::fail);
    }

    private void setupOperations(final @NotNull RouterBuilder routerBuilder) {
        routerBuilder.operation(ServiceHelper.Operation.PERFORM_DELIVERY.toString())
                .handler(this::performDelivery);
        routerBuilder.operation(ServiceHelper.Operation.RESCHEDULE_DELIVERY.toString())
                .handler(this::rescheduleDelivery);
        routerBuilder.operation(ServiceHelper.Operation.LIST_ORDERS.toString())
                .handler(this::listOrders);
        routerBuilder.operation(ServiceHelper.Operation.CALL_BACK.toString())
                .handler(this::callBack);
        routerBuilder.operation(ServiceHelper.Operation.SAVE_DELIVERY.toString())
                .handler(this::saveDelivery);
    }

    private void performDelivery(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final PlacedOrder order = body.getJsonObject(ServiceHelper.ORDER_KEY).mapTo(PlacedOrder.class);
        if (order == null)
            routingContext.response().setStatusCode(CLIENT_ERROR_CODE).end();
        else {
            final DeliveringOrder deliveringOrder = order.deliver();
            REPOSITORY.delivering(deliveringOrder);

            ServiceHelper.sendPerformDeliveryMessage(order.getId(), body.getString(ServiceHelper.COURIER_KEY));
            routingContext.response().end(CORRECT_RESPONSE_TO_PERFORM_DELIVERY);
        }
    }

    private void saveDelivery(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        REPOSITORY.getOrderById(body.getString(ServiceHelper.ORDER_ID_KEY))
                .onSuccess(o -> CastHelper.safeCast(o, DeliveringOrder.class).ifPresent(order -> {
                    final String status = body.getString(ServiceHelper.STATE_KEY);
                    if (ServiceHelper.DELIVERY_SUCCESSFUL.equals(status))
                        REPOSITORY.confirmedDelivery(order.confirmDelivery());
                    else if (ServiceHelper.DELIVERY_FAILED.equals(status))
                        REPOSITORY.failedDelivery(order.failDelivery());
                }));
    }

    private void callBack(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final String orderId = body.getString(ServiceHelper.ORDER_ID_KEY);

        ServiceHelper.sendCallBackMessage(orderId);
    }

    private void rescheduleDelivery(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final FailedOrder order = body.getJsonObject(ServiceHelper.ORDER_KEY).mapTo(FailedOrder.class);
        if (order == null)
            routingContext.response().setStatusCode(CLIENT_ERROR_CODE).end();
        else {
            final Instant newEstimatedArrival =
                    DateHelper.toInstant(body.getString(ServiceHelper.NEW_ESTIMATED_ARRIVAL_KEY));
            final RescheduledOrder rescheduledOrder = order.rescheduleDelivery(newEstimatedArrival);
            REPOSITORY.rescheduled(rescheduledOrder);
            routingContext.response().end(CORRECT_RESPONSE_TO_RESCHEDULE_DELIVERY);
        }
    }

    private void listOrders(final @NotNull RoutingContext routingContext) {
        REPOSITORY.getOrders().onSuccess(orders -> routingContext.response()
                .putHeader("Content-Type", "application/json")
                .send(Json.encodePrettily(orders)));
    }
}
