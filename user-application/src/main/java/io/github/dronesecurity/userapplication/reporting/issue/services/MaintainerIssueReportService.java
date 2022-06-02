/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.issue.services;

import io.github.dronesecurity.userapplication.reporting.issue.entities.OpenIssue;
import io.github.dronesecurity.userapplication.reporting.issue.entities.VisionedIssue;
import io.vertx.core.Future;

/**
 * Service dedicated to issue reporting for the
 * {@link io.github.dronesecurity.userapplication.auth.entities.Maintainer}.
 */
public interface MaintainerIssueReportService extends DataIssueReportService {

    /**
     * Informs the database that an {@link OpenIssue} is now visioned by a maintainer.
     * @param issue the issue that is visioned
     * @return whether the update operation has succeeded or not
     */
    Future<Boolean> visionIssue(OpenIssue issue);

    /**
     * Informs the database that a {@link VisionedIssue} is being closed by a maintainer.
     * @param issue the issue that is closed
     * @param solution string representing the solution used by the maintainer
     * @return whether the update operation has succeeded or not
     */
    Future<Boolean> closeIssue(VisionedIssue issue, String solution);

    /**
     * Get the instance of this service.
     * @return the instance
     */
    static MaintainerIssueReportService getInstance() {
        return IssueReportService.getInstance();
    }
}
