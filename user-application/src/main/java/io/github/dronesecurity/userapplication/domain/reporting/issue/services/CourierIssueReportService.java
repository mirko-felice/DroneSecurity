/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.services;

import io.github.dronesecurity.userapplication.domain.auth.entities.Maintainer;
import io.github.dronesecurity.userapplication.domain.reporting.issue.entities.Issue;
import io.vertx.core.Future;

/**
 * Service dedicated to issue reporting for the
 * {@link Maintainer}.
 */
public interface CourierIssueReportService extends DataIssueReportService {

    /**
     * Creates a new issue.
     * @param issue the issue to send
     * @return the {@link Future} detecting when the insertion is completed
     */
    Future<Void> addIssueReport(Issue issue);

    /**
     * Get the instance of this service.
     * @return the instance
     */
    static CourierIssueReportService getInstance() {
        return IssueReportService.getInstance();
    }
}
