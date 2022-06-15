/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.issue.entities;

import java.time.Instant;

/**
 * An issue that was sent and now created in the system.
 * Differently from the {@link SendingIssue} CreatedIssue has an ID.
 */
public abstract class CreatedIssue extends SendingIssue {

    private final int id;

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
    protected CreatedIssue(final String subject,
                           final int id,
                           final String issueInfo,
                           final String courierUsername,
                           final String assigneeUsername,
                           final Instant sendingTime,
                           final String droneId) {
        super(subject, issueInfo, courierUsername, assigneeUsername, sendingTime, droneId);
        this.id = id;
    }

    /**
     * Gets the ID of the issue.
     *
     * @return the id of the issue
     */
    public int getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object o) {
        if (o == null) return false;
        if (this == o || this.getClass() == o.getClass()) return true;
        if (!CreatedIssue.class.isAssignableFrom(o.getClass())) return false;
        return this.getId() == ((CreatedIssue) o).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[#" + this.id + "] " + this.getSubject();
    }
}
