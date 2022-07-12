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
    }

    private void logIn(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final String username = body.getString(UserAPIHelper.USERNAME_KEY);
        final String password = body.getString(UserAPIHelper.PASSWORD_KEY);

        final boolean logIn = this.authenticationService.logIn(Username.parse(username), password);
        routingContext.response().end(Boolean.toString(logIn));
    }

    private void logOut(final @NotNull RoutingContext routingContext) {
        final boolean logOut = this.authenticationService.logOut();
        routingContext.response().end(Boolean.toString(logOut));
    }

    private void retrieveCourierByUsername(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final String username = body.getString(UserAPIHelper.USERNAME_KEY);

        final Optional<Courier> courier = this.userManager.retrieveCourierByUsername(Username.parse(username));
        if (courier.isPresent())
            routingContext.response().end(Json.encodePrettily(OpenHostService.convertToCourier(courier.get())));
        else
            routingContext.response().setStatusCode(NOT_AVAILABLE).end();
    }

    private void retrieveMaintainerByUsername(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final String username = body.getString(UserAPIHelper.USERNAME_KEY);

        final Optional<Maintainer> maintainer = this.userManager.retrieveMaintainerByUsername(Username.parse(username));
        if (maintainer.isPresent())
            routingContext.response().end(Json.encodePrettily(OpenHostService.convertToGenericUser(maintainer.get())));
        else
            routingContext.response().setStatusCode(NOT_AVAILABLE).end();
    }

    private void checkLoggedUserRole(final RoutingContext routingContext) {
        final Optional<Role> loggedUserRole = this.userManager.checkLoggedUserRole();
        if (loggedUserRole.isPresent())
            routingContext.response().end(Json.encodePrettily(OpenHostService.convertToUserRole(loggedUserRole.get())));
        else
            routingContext.response().end(UserRole.NOT_LOGGED.toString());
    }

    private void retrieveLoggedCourierIfPresent(final @NotNull RoutingContext routingContext) {
        final Optional<Courier> loggedCourier = this.userManager.retrieveLoggedCourierIfPresent();
        if (loggedCourier.isPresent())
            routingContext.response().end(Json.encodePrettily(OpenHostService.convertToCourier(loggedCourier.get())));
        else
            routingContext.response().setStatusCode(NOT_AVAILABLE).end();
    }

    private void retrieveLoggedMaintainerIfPresent(final @NotNull RoutingContext routingContext) {
        final Optional<Maintainer> loggedMaintainer = this.userManager.retrieveLoggedMaintainerIfPresent();
        if (loggedMaintainer.isPresent())
            routingContext.response().end(
                    Json.encodePrettily(OpenHostService.convertToGenericUser(loggedMaintainer.get())));
        else
            routingContext.response().setStatusCode(NOT_AVAILABLE).end();
    }

}
