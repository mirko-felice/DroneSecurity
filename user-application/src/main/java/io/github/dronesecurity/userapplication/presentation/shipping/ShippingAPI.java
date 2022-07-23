/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.shipping;

import io.github.dronesecurity.userapplication.application.shipping.DeliveryServiceImpl;
import io.github.dronesecurity.userapplication.application.shipping.OrderManagerImpl;
import io.github.dronesecurity.userapplication.domain.shipping.client.Client;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.DeliveringOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.FailedOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.PlacedOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.Product;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.repo.OrderRepository;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.service.DeliveryService;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.service.OrderManager;
import io.github.dronesecurity.userapplication.infrastructure.shipping.repo.MongoOrderRepository;
import io.github.dronesecurity.userapplication.presentation.AbstractAPI;
import io.github.dronesecurity.lib.utilities.CastHelper;
import io.github.dronesecurity.userapplication.utilities.shipping.ShippingAPIHelper;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import org.jetbrains.annotations.NotNull;

/**
 * API exposing operations related to the shipping.
 */
public final class ShippingAPI extends AbstractAPI {

    private static final String OPEN_API_URL = AbstractAPI.BASE_API_URL + "shipping/shippingAPI.json";
    private static final int CLIENT_ERROR_CODE = 400;
    private final DeliveryService deliveryService;
    private final OrderManager orderManager;

    /**
     * Build the API.
     */
    public ShippingAPI() {
        final OrderRepository orderRepository = new MongoOrderRepository();
        this.deliveryService = new DeliveryServiceImpl(orderRepository);
        this.orderManager = new OrderManagerImpl(orderRepository);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getOpenAPISpecUrl() {
        return OPEN_API_URL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setupOperations(final @NotNull RouterBuilder routerBuilder) {
        routerBuilder.operation(ShippingAPIHelper.Operation.PLACE_ORDER.toString())
                .handler(this::placeOrder);
        routerBuilder.operation(ShippingAPIHelper.Operation.LIST_ORDERS.toString())
                .handler(this::listOrders);
        routerBuilder.operation(ShippingAPIHelper.Operation.PERFORM_DELIVERY.toString())
                .handler(this::performDelivery);
        routerBuilder.operation(ShippingAPIHelper.Operation.SUCCEED_DELIVERY.toString())
                .handler(this::succeedDelivery);
        routerBuilder.operation(ShippingAPIHelper.Operation.FAIL_DELIVERY.toString())
                .handler(this::failDelivery);
        routerBuilder.operation(ShippingAPIHelper.Operation.RESCHEDULE_DELIVERY.toString())
                .handler(this::rescheduleDelivery);
    }

    private void listOrders(final @NotNull RoutingContext routingContext) {
        routingContext.response()
                .putHeader("Content-Type", "application/json")
                .send(Json.encodePrettily(this.executeSync(this.orderManager::listOrders)));
    }

    private void placeOrder(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final Client client = Client.mock(body.getString(ShippingAPIHelper.CLIENT_NAME_KEY));
        final Product product = Product.fromName(body.getString(ShippingAPIHelper.PRODUCT_NAME_KEY));
        final OrderDate estimatedArrival =
                OrderDate.parseString(body.getString(ShippingAPIHelper.ESTIMATED_ARRIVAL_KEY));
        this.executeSync(() -> this.orderManager.placeOrder(client, product, estimatedArrival));
        routingContext.response().end();
    }

    private void performDelivery(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final long id = params.body().getJsonObject().getLong(ShippingAPIHelper.ORDER_ID_KEY);
        final Order order = this.orderManager.retrieveOrderById(OrderIdentifier.fromLong(id));
        CastHelper.safeCast(order, PlacedOrder.class).ifPresentOrElse(placedOrder -> {
            final String droneId = body.getString(ShippingAPIHelper.DRONE_ID_KEY);
            final String courierUsername = body.getString(ShippingAPIHelper.COURIER_USERNAME_KEY);
            this.executeSync(() -> this.deliveryService.performDelivery(placedOrder, droneId, courierUsername));
            routingContext.response().end();
        }, () -> routingContext.response().setStatusCode(CLIENT_ERROR_CODE).end());
    }

    private void succeedDelivery(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final long id = params.body().getJsonObject().getLong(ShippingAPIHelper.ORDER_ID_KEY);
        final Order order = this.orderManager.retrieveOrderById(OrderIdentifier.fromLong(id));
        CastHelper.safeCast(order, DeliveringOrder.class).ifPresentOrElse(deliveringOrder -> {
            this.executeSync(() -> this.deliveryService.succeedDelivery(deliveringOrder));
            routingContext.response().end();
        }, () -> routingContext.response().setStatusCode(CLIENT_ERROR_CODE).end());
    }

    private void failDelivery(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final long id = params.body().getJsonObject().getLong(ShippingAPIHelper.ORDER_ID_KEY);
        final Order order = this.orderManager.retrieveOrderById(OrderIdentifier.fromLong(id));
        CastHelper.safeCast(order, DeliveringOrder.class).ifPresentOrElse(deliveringOrder -> {
            this.executeSync(() -> this.deliveryService.failDelivery(deliveringOrder));
            routingContext.response().end();
        }, () -> routingContext.response().setStatusCode(CLIENT_ERROR_CODE).end());
    }

    private void rescheduleDelivery(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final long id = body.getLong(ShippingAPIHelper.ORDER_ID_KEY);
        final Order order = this.orderManager.retrieveOrderById(OrderIdentifier.fromLong(id));
        CastHelper.safeCast(order, FailedOrder.class).ifPresentOrElse(failedOrder -> {
            final OrderDate newEstimatedArrival =
                    OrderDate.parseString(body.getString(ShippingAPIHelper.NEW_ESTIMATED_ARRIVAL_KEY));
            this.executeSync(() -> this.deliveryService.rescheduleDelivery(failedOrder, newEstimatedArrival));
            routingContext.response().end();
        }, () -> routingContext.response().setStatusCode(CLIENT_ERROR_CODE).end());
    }
}
