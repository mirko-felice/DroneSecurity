/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.openissue.entities;

import io.github.dronesecurity.userapplication.domain.reporting.issue.activeissue.entities.AbstractActiveIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.entities.VisionedIssue;
import io.github.dronesecurity.userapplication.infrastructure.reporting.issue.serializers.IssueStringHelper;

import java.time.Instant;

/**
 * Class representing an issue that has been created and sent.
 */
public class OpenIssue extends AbstractActiveIssue {

    /**
     * Builds a new issue report.
     *
     * @param subject the short description of the issue (alias title/subject)
     * @param id the id of the created issue
     * @param issueInfo the information regarding the issue report
     * @param courierUsername username of the courier who sends the issue
     * @param assigneeUsername username of the maintainer assigned to the issue
     * @param sendingTime the timestamp of when the issue was sent
     * @param droneId drone identifier that owns the issue
     */
    public OpenIssue(final String subject,
                     final long id,
                     final String issueInfo,
                     final String courierUsername,
                     final String assigneeUsername,
                     final Instant sendingTime,
                     final String droneId) {
        super(subject, id, issueInfo, courierUsername, assigneeUsername, sendingTime, droneId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getState() {
        return IssueStringHelper.STATUS_OPEN;
    }

    /**
     * Transforms this issue in visioned issue when the maintainer starts processing it.
     * @return the new {@link VisionedIssue} from this OpenIssue
     */
    public VisionedIssue visionIssue() {

        return new VisionedIssue(this.getSubject(),
                this.getId().getIssueId(),
                this.getDetails(),
                this.getCourier(),
                this.assignedTo(),
                this.getReportingDate(),
                this.getDroneId());
    }
}
