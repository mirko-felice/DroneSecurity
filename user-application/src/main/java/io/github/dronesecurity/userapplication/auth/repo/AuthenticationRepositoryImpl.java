/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.auth.repo;

import io.github.dronesecurity.userapplication.auth.entities.Courier;
import io.github.dronesecurity.userapplication.auth.entities.LoggedUser;
import io.github.dronesecurity.userapplication.auth.entities.Maintainer;
import io.github.dronesecurity.userapplication.auth.entities.NotLoggedUser;
import io.github.dronesecurity.userapplication.auth.utilities.UserConstants;
import io.github.dronesecurity.userapplication.utilities.PasswordHelper;
import io.github.dronesecurity.userapplication.utilities.VertxHelper;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

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
                            return Future.succeededFuture(Json.decodeValue(res.result().toBuffer(), LoggedUser.class));
                        } else
                            return Future.failedFuture(res.cause());
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                        return Future.failedFuture(e);
                    }
                });
    }

    @Override
    public Future<Courier> retrieveCourier(final String username) {
        return this.retrieveUserFromUsername(username).map(user -> Json.decodeValue(user.toBuffer(), Courier.class));
    }

    @Override
    public Future<Maintainer> retrieveMaintainer(final String username) {
        return this.retrieveUserFromUsername(username).map(user -> Json.decodeValue(user.toBuffer(), Maintainer.class));
    }

    private Future<JsonObject> retrieveUserFromUsername(final String username) {
        final JsonObject query = new JsonObject().put(UserConstants.USERNAME, username);
        return VertxHelper.MONGO_CLIENT.findOne(COLLECTION_NAME, query, null);
    }
}
