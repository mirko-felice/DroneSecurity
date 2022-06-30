/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.services;

import io.github.dronesecurity.userapplication.domain.auth.entities.Maintainer;
import io.github.dronesecurity.userapplication.domain.reporting.issue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.entities.Issue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.entities.OpenIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.entities.VisionedIssue;
import io.vertx.core.Future;

/**
 * Service dedicated to issue reporting for the
 * {@link Maintainer}.
 */
public interface MaintainerIssueReportService extends DataIssueReportService {

    /**
     * Informs the database that an {@link OpenIssue} is now visioned by a maintainer.
     * @param issue the issue that is visioned
     * @return whether the update operation has succeeded or not
     */
    Future<Boolean> visionIssue(VisionedIssue issue);

    /**
     * Informs the database that an {@link Issue} has been closed by a maintainer
     * and there is a new {@link ClosedIssue} in the system.
     * @param issue the issue that was closed
     * @return whether the update operation has succeeded or not
     */
    Future<Boolean> closeIssue(ClosedIssue issue);

    /**
     * Get the instance of this service.
     * @return the instance
     */
    static MaintainerIssueReportService getInstance() {
        return IssueReportService.getInstance();
    }
}
