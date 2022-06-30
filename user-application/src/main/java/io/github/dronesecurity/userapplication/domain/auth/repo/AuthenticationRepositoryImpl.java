/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.auth.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.userapplication.domain.auth.entities.*;
import io.github.dronesecurity.userapplication.domain.auth.utilities.UserConstants;
import io.github.dronesecurity.userapplication.utilities.PasswordHelper;
import io.github.dronesecurity.userapplication.utilities.VertxHelper;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Implementation of {@link AuthenticationRepository}.
 */
public final class AuthenticationRepositoryImpl implements AuthenticationRepository {

    private static final String COLLECTION_NAME = "users";
    private static AuthenticationRepositoryImpl singleton;

    /**
     * Get the Singleton instance.
     * @return the singleton
     */
    public static AuthenticationRepository getInstance() {
        synchronized (AuthenticationRepositoryImpl.class) {
            if (singleton == null)
                singleton = new AuthenticationRepositoryImpl();
            return singleton;
        }
    }

    @Override
    public Future<LoggedUser> authenticate(final @NotNull NotLoggedUser user) {
        final JsonObject query = new JsonObject().put(UserConstants.USERNAME, user.getUsername());
        return VertxHelper.MONGO_CLIENT.findOne(COLLECTION_NAME, query, null)
                .transform(res -> {
                    try {
                        if (res.succeeded() && PasswordHelper.validatePassword(user.getPassword(),
                                res.result().getString(UserConstants.PASSWORD))) {
                            return this.deserializeUser(res.result(), LoggedUser.class);
                        } else
                            return Future.failedFuture(res.cause());
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                        return Future.failedFuture(e);
                    }
                });
    }

    @Override
    public Future<Courier> retrieveCourier(final String username) {
        return this.retrieveUserFromUsername(username).flatMap(user -> this.deserializeUser(user, Courier.class));
    }

    @Override
    public Future<Maintainer> retrieveMaintainer(final String username) {
        return this.retrieveUserFromUsername(username).flatMap(user -> this.deserializeUser(user, Maintainer.class));
    }

    private Future<JsonObject> retrieveUserFromUsername(final String username) {
        final JsonObject query = new JsonObject().put(UserConstants.USERNAME, username);
        return VertxHelper.MONGO_CLIENT.findOne(COLLECTION_NAME, query, null);
    }

    private <T extends LoggedUser> Future<T> deserializeUser(final @NotNull JsonObject json, final Class<T> clazz) {
        if (Role.valueOf(json.getString(UserConstants.ROLE)) == Role.COURIER)
            return Future.succeededFuture(Json.decodeValue(json.toBuffer(), clazz));
        else {
            final JsonObject query = new JsonObject();
            query.put(UserConstants.ROLE, Role.COURIER.toString());
            query.put(UserConstants.SUPERVISOR, json.getString(UserConstants.USERNAME));
            return VertxHelper.MONGO_CLIENT.find(COLLECTION_NAME, query).map(objects -> {
                try {
                    final String[] couriers = objects.stream()
                            .map(c -> c.getString(UserConstants.USERNAME))
                            .toArray(String[]::new);
                    return new ObjectMapper().reader()
                            .forType(Maintainer.class)
                            .withAttribute(UserConstants.COURIERS, couriers)
                            .readValue(json.toString());
                } catch (JsonProcessingException e) {
                    LoggerFactory.getLogger(this.getClass()).error("Can NOT deserialize Maintainer.", e);
                    return null;
                }
            });
        }
    }
}
