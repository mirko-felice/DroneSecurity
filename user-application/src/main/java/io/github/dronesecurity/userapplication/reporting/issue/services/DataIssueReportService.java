/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.issue.services;

import io.github.dronesecurity.userapplication.reporting.issue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.reporting.issue.entities.CreatedIssue;
import io.github.dronesecurity.userapplication.reporting.issue.entities.OpenIssue;
import io.vertx.core.Future;

import java.util.List;
import java.util.function.Consumer;

/**
 * Service dedicated to issue reporting.
 */
public interface DataIssueReportService {

    /**
     * Lists open issues reported.
     * @return the future of the list of open issues reported
     */
    Future<List<CreatedIssue>> getOpenIssueReports();

    /**
     * Lists issues reported that have been closed.
     * @return the future of the list of closed issues
     */
    Future<List<ClosedIssue>> getClosedIssueReports();

    /**
     * Subscribes to new issues created by any courier.
     * @param consumer consumes the {@link OpenIssue}
     */
    void subscribeToNewIssue(Consumer<OpenIssue> consumer);
}
