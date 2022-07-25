/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.reporting.issue.repo;

import io.github.dronesecurity.userapplication.domain.reporting.issue.createdissue.entities.AbstractCreatedIssue;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Repository helper that initializes a json body with the data of the logged user.
 */
public final class IssueRetrievalHelper {

    private IssueRetrievalHelper() { }

    /**
     * Executes the query to retrieve issues.
     * @param callable query as callable
     * @param <T> type parameter
     * @return list of all issues retrieved
     */
    public static <T extends AbstractCreatedIssue> List<T> executeSync(
            final @NotNull Callable<List<T>> callable) {
        try {
            final ExecutorService executor = Executors.newSingleThreadExecutor();
            final Future<List<T>> result = executor.submit(callable);
            executor.shutdown();
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }
}
