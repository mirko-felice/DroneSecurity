/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.user.repo;

import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Courier;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Maintainer;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.User;
import io.github.dronesecurity.userapplication.domain.user.objects.Username;
import io.github.dronesecurity.userapplication.domain.user.repo.UserRepository;
import io.github.dronesecurity.userapplication.infrastructure.user.UserConstants;
import io.github.dronesecurity.userapplication.utilities.user.PasswordHelper;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Implementation of {@link UserRepository} to work with the underlying DB.
 */
public final class MongoUserRepository extends AbstractUserRepository {

    private static final String COLLECTION_NAME = "users";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAuthenticated(final @NotNull Username username, final String password) {
        final JsonObject query = new JsonObject().put(UserConstants.USERNAME, username.asString());
        return Boolean.TRUE.equals(this.waitFutureResult(this.mongo().findOne(COLLECTION_NAME, query, null)
                .transform(res -> {
                    if (res.succeeded()) {
                        final String storedPassword = res.result().getString(UserConstants.PASSWORD);
                        return Future.succeededFuture(PasswordHelper.validatePassword(password, storedPassword));
                    } else
                        return Future.succeededFuture(Boolean.FALSE);
                })));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Courier> retrieveCourierByUsername(final Username username) {
        return this.waitFutureResult(this.retrieveUserFromUsername(username)
                .map(user -> Optional.of(Json.decodeValue(user.toBuffer(), Courier.class)))
                .otherwise(Optional.empty()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Maintainer> retrieveMaintainerByUsername(final Username username) {
        return this.waitFutureResult(this.retrieveUserFromUsername(username)
                .map(user -> Optional.of(Json.decodeValue(user.toBuffer(), Maintainer.class)))
                .otherwise(Optional.empty()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> retrieveUserByUsername(final Username username) {
        return this.waitFutureResult(this.retrieveUserFromUsername(username)
                .map(user -> Optional.of(Json.decodeValue(user.toBuffer(), User.class)))
                .otherwise(Optional.empty()));
    }

    private Future<JsonObject> retrieveUserFromUsername(final @NotNull Username username) {
        final JsonObject query = new JsonObject().put(UserConstants.USERNAME, username.asString());
        return this.mongo().findOne(COLLECTION_NAME, query, null);
    }

}
