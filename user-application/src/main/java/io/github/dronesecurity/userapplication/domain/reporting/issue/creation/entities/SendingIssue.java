/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.creation.entities;

import io.github.dronesecurity.userapplication.infrastructure.reporting.issue.serializers.IssueStringHelper;

import java.time.Instant;

/**
 * Entity representing an issue report that has to be sent and created.
 */
public class SendingIssue implements Issue {

    private final String subject;
    private final String issueInfo;
    private final String courierUsername;
    private final String assigneeUsername;
    private final Instant sendingTime;
    private final String droneId;

    /**
     * Builds a new issue report.
     * @param subject the short description of the issue (alias title/subject)
     * @param issueInfo the information regarding the issue report
     * @param courierUsername username of the courier who sends the issue
     * @param assigneeUsername username of the maintainer assigned to the issue
     * @param sendingTime the timestamp of when the issue was sent
     * @param droneId drone identifier that owns the issue
     */
    public SendingIssue(final String subject,
                        final String issueInfo,
                        final String courierUsername,
                        final String assigneeUsername,
                        final Instant sendingTime,
                        final String droneId) {
        this.subject = subject;
        this.issueInfo = issueInfo;
        this.courierUsername = courierUsername;
        this.assigneeUsername = assigneeUsername;
        this.sendingTime = sendingTime;
        this.droneId = droneId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSubject() {
        return this.subject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDetails() {
        return this.issueInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCourier() {
        return this.courierUsername;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String assignedTo() {
        return this.assigneeUsername;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getReportingDate() {
        return this.sendingTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getState() {
        return IssueStringHelper.STATUS_OPEN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDroneId() {
        return this.droneId;
    }
}
