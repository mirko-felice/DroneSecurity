/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.shipping;

import io.github.dronesecurity.lib.DrivingMode;
import io.github.dronesecurity.userapplication.application.shipping.DroneControllerImpl;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.service.DroneController;
import io.github.dronesecurity.userapplication.presentation.AbstractAPI;
import io.github.dronesecurity.userapplication.utilities.shipping.DroneAPIHelper;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import org.jetbrains.annotations.NotNull;

/**
 * API exposing operations related to the drone.
 */
public final class DroneAPI extends AbstractAPI {

    private static final String OPEN_API_URL = AbstractAPI.BASE_API_URL + "shipping/droneAPI.json";
    private final DroneController droneController;

    /**
     * Build the API.
     */
    public DroneAPI() {
        this.droneController = new DroneControllerImpl();
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
        routerBuilder.operation(DroneAPIHelper.Operation.CALL_BACK.toString())
                .handler(this::callBack);
        routerBuilder.operation(DroneAPIHelper.Operation.CHANGE_MODE.toString())
                .handler(this::changeMode);
        routerBuilder.operation(DroneAPIHelper.Operation.PROCEED.toString())
                .handler(this::proceed);
        routerBuilder.operation(DroneAPIHelper.Operation.HALT.toString())
                .handler(this::halt);
    }

    private void callBack(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final long orderId = body.getLong(DroneAPIHelper.ORDER_ID_KEY);

        this.droneController.callBack(OrderIdentifier.fromLong(orderId));
        routingContext.response().end();
    }

    private void changeMode(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final long orderId = body.getLong(DroneAPIHelper.ORDER_ID_KEY);
        final DrivingMode drivingMode = DrivingMode.valueOf(body.getString(DroneAPIHelper.DRIVING_MODE_KEY));

        this.droneController.changeDrivingMode(OrderIdentifier.fromLong(orderId), drivingMode);
        routingContext.response().end();
    }

    private void proceed(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final long orderId = body.getLong(DroneAPIHelper.ORDER_ID_KEY);

        this.droneController.proceed(OrderIdentifier.fromLong(orderId));
        routingContext.response().end();
    }

    private void halt(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final long orderId = body.getLong(DroneAPIHelper.ORDER_ID_KEY);

        this.droneController.halt(OrderIdentifier.fromLong(orderId));
        routingContext.response().end();
    }
}
