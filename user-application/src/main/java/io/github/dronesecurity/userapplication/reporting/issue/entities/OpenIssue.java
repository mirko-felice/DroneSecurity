/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.issue.entities;

import io.github.dronesecurity.userapplication.reporting.issue.serialization.IssueStringHelper;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Class representing an issue that has been created and sent.
 */
public class OpenIssue extends CreatedIssue {

    /**
     * Builds a new issue report.
     *
     * @param subject the short description of the issue (alias title/subject)
     * @param id the id of the created issue
     * @param issueInfo the information regarding the issue report
     * @param courierUsername username of the courier who sends the issue
     * @param sendingTime the timestamp of when the issue was sent
     */
    public OpenIssue(final String subject,
                     @NotNull final int id,
                     final String issueInfo,
                     final String courierUsername,
                     final Instant sendingTime) {
        super(subject, id, issueInfo, courierUsername, sendingTime);
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
                this.getId(),
                this.getDetails(),
                this.getCourier(),
                this.getReportingDate());
    }
}
