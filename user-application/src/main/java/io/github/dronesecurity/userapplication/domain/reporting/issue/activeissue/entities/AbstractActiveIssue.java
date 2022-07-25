/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.activeissue.entities;

import io.github.dronesecurity.userapplication.domain.reporting.issue.createdissue.entities.AbstractCreatedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.openissue.entities.OpenIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.entities.VisionedIssue;

import java.time.Instant;

/**
 * An active issue is an issue that wasn't closed yet. Currently, it's either an {@link OpenIssue} or a
 * {@link VisionedIssue}.
 */
public abstract class AbstractActiveIssue extends AbstractCreatedIssue {

    /**
     * Builds a new issue report.
     *
     * @param subject          the short description of the issue (alias title/subject)
     * @param id               the id of the created issue
     * @param issueInfo        the information regarding the issue report
     * @param courierUsername  username of the courier who sends the issue
     * @param assigneeUsername username of the maintainer assigned to the issue
     * @param sendingTime      the timestamp of when the issue was sent
     * @param droneId          drone identifier that owns the issue
     */
    protected AbstractActiveIssue(final String subject,
                               final long id,
                               final String issueInfo,
                               final String courierUsername,
                               final String assigneeUsername,
                               final Instant sendingTime,
                               final String droneId) {
        super(subject, id, issueInfo, courierUsername, assigneeUsername, sendingTime, droneId);
    }
}
