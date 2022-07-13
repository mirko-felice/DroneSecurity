/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.user;

import io.github.dronesecurity.userapplication.application.user.AuthenticationServiceImpl;
import io.github.dronesecurity.userapplication.application.user.UserManagerImpl;
import io.github.dronesecurity.userapplication.application.user.ohs.OpenHostService;
import io.github.dronesecurity.userapplication.application.user.ohs.pl.UserRole;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Courier;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Maintainer;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Role;
import io.github.dronesecurity.userapplication.domain.user.objects.Username;
import io.github.dronesecurity.userapplication.domain.user.repo.UserRepository;
import io.github.dronesecurity.userapplication.domain.user.services.AuthenticationService;
import io.github.dronesecurity.userapplication.domain.user.services.UserManager;
import io.github.dronesecurity.userapplication.infrastructure.user.repo.MongoUserRepository;
import io.github.dronesecurity.userapplication.presentation.AbstractAPI;
import io.github.dronesecurity.userapplication.utilities.user.UserAPIHelper;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * API exposing operations related to the user.
 */
public final class UserAPI extends AbstractAPI {

    private static final String OPEN_API_URL = AbstractAPI.BASE_API_URL + "user/userAPI.json";
    private static final int NOT_AVAILABLE = 404;
    private final AuthenticationService authenticationService;
    private final UserManager userManager;

    /**
     * Build the API.
     */
    public UserAPI() {
        final UserRepository userRepository = new MongoUserRepository();
        this.authenticationService = new AuthenticationServiceImpl(userRepository);
        this.userManager = new UserManagerImpl(userRepository);
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
        routerBuilder.operation(UserAPIHelper.Operation.LOG_IN.toString())
                .handler(this::logIn);
        routerBuilder.operation(UserAPIHelper.Operation.LOG_OUT.toString())
                .handler(this::logOut);
        routerBuilder.operation(UserAPIHelper.Operation.RETRIEVE_COURIER_BY_USERNAME.toString())
                .handler(this::retrieveCourierByUsername);
        routerBuilder.operation(UserAPIHelper.Operation.RETRIEVE_MAINTAINER_BY_USERNAME.toString())
                .handler(this::retrieveMaintainerByUsername);
        routerBuilder.operation(UserAPIHelper.Operation.CHECK_LOGGED_USER_ROLE.toString())
                .handler(this::checkLoggedUserRole);
        routerBuilder.operation(UserAPIHelper.Operation.RETRIEVE_LOGGED_COURIER_IF_PRESENT.toString())
                .handler(this::retrieveLoggedCourierIfPresent);
        routerBuilder.operation(UserAPIHelper.Operation.RETRIEVE_LOGGED_MAINTAINER_IF_PRESENT.toString())
                .handler(this::retrieveLoggedMaintainerIfPresent);
        routerBuilder.operation(UserAPIHelper.Operation.ADD_DRONE.toString())
                .handler(this::addDrone);
        routerBuilder.operation(UserAPIHelper.Operation.REMOVE_DRONE.toString())
                .handler(this::removeDrone);
    }

    private void logIn(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final String username = body.getString(UserAPIHelper.USERNAME_KEY);
        final String password = body.getString(UserAPIHelper.PASSWORD_KEY);

        final Boolean logIn = this.executeSync(() ->
                this.authenticationService.logIn(Username.parse(username), password));
        routingContext.response().end(logIn == null ? Boolean.FALSE.toString() : logIn.toString());
    }

    private void logOut(final @NotNull RoutingContext routingContext) {
        final Boolean logOut = this.executeSync(this.authenticationService::logOut);
        routingContext.response().end(logOut == null ? Boolean.FALSE.toString() : logOut.toString());
    }

    private void retrieveCourierByUsername(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final String username = body.getString(UserAPIHelper.USERNAME_KEY);

        final Optional<Courier> courier = Optional.ofNullable(this.executeSync(() ->
                this.userManager.retrieveCourierByUsername(Username.parse(username)).orElse(null)));
        if (courier.isPresent())
            routingContext.response().end(Json.encodePrettily(OpenHostService.convertToCourier(courier.get())));
        else
            routingContext.response().setStatusCode(NOT_AVAILABLE).end();
    }

    private void retrieveMaintainerByUsername(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final String username = body.getString(UserAPIHelper.USERNAME_KEY);

        final Optional<Maintainer> maintainer = Optional.ofNullable(this.executeSync(() ->
                this.userManager.retrieveMaintainerByUsername(Username.parse(username)).orElse(null)));
        if (maintainer.isPresent())
            routingContext.response().end(Json.encodePrettily(OpenHostService.convertToGenericUser(maintainer.get())));
        else
            routingContext.response().setStatusCode(NOT_AVAILABLE).end();
    }

    private void checkLoggedUserRole(final RoutingContext routingContext) {
        final Optional<Role> loggedUserRole = Optional.ofNullable(this.executeSync(() ->
                this.userManager.checkLoggedUserRole().orElse(null)));
        if (loggedUserRole.isPresent())
            routingContext.response().end(OpenHostService.convertToUserRole(loggedUserRole.get()).toString());
        else
            routingContext.response().end(UserRole.NOT_LOGGED.toString());
    }

    private void retrieveLoggedCourierIfPresent(final @NotNull RoutingContext routingContext) {
        final Optional<Courier> loggedCourier = Optional.ofNullable(this.executeSync(() ->
                this.userManager.retrieveLoggedCourierIfPresent().orElse(null)));
        if (loggedCourier.isPresent())
            routingContext.response().end(Json.encodePrettily(OpenHostService.convertToCourier(loggedCourier.get())));
        else
            routingContext.response().setStatusCode(NOT_AVAILABLE).end();
    }

    private void retrieveLoggedMaintainerIfPresent(final @NotNull RoutingContext routingContext) {
        final Optional<Maintainer> loggedMaintainer = Optional.ofNullable(this.executeSync(() ->
                this.userManager.retrieveLoggedMaintainerIfPresent().orElse(null)));
        if (loggedMaintainer.isPresent())
            routingContext.response().end(
                    Json.encodePrettily(OpenHostService.convertToGenericUser(loggedMaintainer.get())));
        else
            routingContext.response().setStatusCode(NOT_AVAILABLE).end();
    }

    private void addDrone(final @NotNull RoutingContext routingContext) {
        this.updateDrones(routingContext, Courier::addDrone);
    }

    private void removeDrone(final @NotNull RoutingContext routingContext) {
        this.updateDrones(routingContext, Courier::removeDrone);
    }

    private void updateDrones(final @NotNull RoutingContext routingContext, final BiConsumer<Courier, String> update) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final String drone = body.getString(UserAPIHelper.DRONE_KEY);
        final Optional<Courier> loggedCourier = Optional.ofNullable(this.executeSync(() ->
                this.userManager.retrieveLoggedCourierIfPresent().orElse(null)));
        if (loggedCourier.isPresent()) {
            update.accept(loggedCourier.get(), drone);
            routingContext.response().end();
        } else
            routingContext.response().setStatusCode(NOT_AVAILABLE).end();
    }
}
