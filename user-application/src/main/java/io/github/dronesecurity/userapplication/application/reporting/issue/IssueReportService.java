/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.reporting.issue;

import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.creation.entities.Issue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.creation.entities.SendingIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.openissue.entities.OpenIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.entities.VisionedIssue;

/**
 * Interface representing a service for issue reporting.
 */
public interface IssueReportService extends DataIssueReportService {

    /**
     * Creates a new issue.
     * @param issue the issue to send
     */
    void addIssueReport(SendingIssue issue);

    /**
     * Informs the database that an {@link OpenIssue} is now visioned by a maintainer.
     * @param issue the issue that is visioned
     * @return whether the update operation has succeeded or not
     */
    boolean visionIssue(VisionedIssue issue);

    /**
     * Informs the database that an {@link Issue} has been closed by a maintainer
     * and there is a new {@link ClosedIssue} in the system.
     * @param issue the issue that was closed
     * @return whether the update operation has succeeded or not
     */
    boolean closeIssue(ClosedIssue issue);
}
