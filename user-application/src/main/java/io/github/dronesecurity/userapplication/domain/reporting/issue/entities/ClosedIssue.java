/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.entities;

import io.github.dronesecurity.userapplication.domain.reporting.issue.serialization.IssueStringHelper;

import java.time.Instant;

/**
 * Class representing a solved issue report.
 */
public class ClosedIssue extends CreatedIssue {

    private final String issueSolution;

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
     * @param issueSolution string representing the solution used by the maintainer
     */
    public ClosedIssue(final String subject,
                       final int id,
                       final String issueInfo,
                       final String courierUsername,
                       final String assigneeUsername,
                       final Instant sendingTime,
                       final String droneId,
                       final String issueSolution) {
        super(subject, id, issueInfo, courierUsername, assigneeUsername, sendingTime, droneId);
        this.issueSolution = issueSolution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getState() {
        return IssueStringHelper.STATUS_CLOSED;
    }

    /**
     * Gets the solution specified by the maintainer to the issue.
     * @return the string describing the solution chosen by the maintainer
     */
    public String getIssueSolution() {
        return this.issueSolution;
    }
}
