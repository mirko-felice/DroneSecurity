/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure;

import io.github.dronesecurity.userapplication.utilities.VertxHelper;
import io.vertx.core.Future;
import io.vertx.ext.mongo.MongoClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Base class for all repositories working with Mongo.
 */
public class MongoRepository {

    /**
     * Protected constructor to restrict instantiation only to subclasses.
     */
    protected MongoRepository() {
        // Empty default constructor needed to restrict instantiation only to subclasses.
    }

    /**
     * Retrieves mongo db interface.
     * @return the {@link MongoClient}
     */
    protected MongoClient mongo() {
        return VertxHelper.MONGO_CLIENT;
    }

    /**
     * Waits for {@link Future} result.
     * @param future {@link Future} to wait for
     * @param <T> type parameter of the {@code future}
     * @return the result of the {@code future}
     */
    protected <T> @Nullable T waitFutureResult(final @NotNull Future<T> future) {
        try {
            return future.toCompletionStage().toCompletableFuture().get(2, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
