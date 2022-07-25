/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.entities;

import io.github.dronesecurity.userapplication.domain.reporting.issue.createdissue.entities.AbstractCreatedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.infrastructure.reporting.issue.serializers.IssueStringHelper;

import java.time.Instant;

/**
 * Class representing an open issue that is currently visioned by a maintainer.
 */
public class VisionedIssue extends AbstractCreatedIssue {

    /**
     * Builds a new Visioned Issue report.
     *
     * @param subject         the short description of the issue (alias title/subject)
     * @param id              the id of the created issue
     * @param issueInfo       the information regarding the issue report
     * @param courierUsername username of the courier who sends the issue
     * @param assigneeUsername username of the maintainer assigned to the issue
     * @param sendingTime     the timestamp of when the issue was sent
     * @param droneId         drone identifier that owns the issue
     */
    public VisionedIssue(final String subject,
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
        return IssueStringHelper.STATUS_VISIONED;
    }

    /**
     * Transforms this issue in closed issue when the maintainer completes his visioning.
     * @param solution string representing the solution used by the maintainer
     * @return the new {@link ClosedIssue} from this VisionedIssue
     */
    public ClosedIssue closeIssue(final String solution) {
        return new ClosedIssue(this.getSubject(),
                this.getId().getId(),
                this.getDetails(),
                this.getCourier(),
                this.assignedTo(),
                this.getReportingDate(),
                this.getDroneId(),
                solution);
    }
}
