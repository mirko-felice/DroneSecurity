/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.reporting.issue.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.lib.utilities.DateHelper;
import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.creation.entities.Issue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.creation.entities.SendingIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.openissue.entities.OpenIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.entities.VisionedIssue;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Instant;

/**
 * Deserialize the {@link Issue}.
 */
public class IssueDeserializer extends JsonDeserializer<Issue> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Issue deserialize(final @NotNull JsonParser parser, final DeserializationContext ctxt)
            throws IOException {
        final ObjectNode root = parser.getCodec().readTree(parser);
        final String subject = root.get(IssueStringHelper.SUBJECT).asText();
        final String details = root.get(IssueStringHelper.DETAILS).asText();
        final String courierUsername = root.get(IssueStringHelper.COURIER).asText();
        final String assigneeUsername = root.get(IssueStringHelper.ASSIGNEE).asText();
        final Instant sendingInstant = DateHelper.toInstant(root.get(IssueStringHelper.SENDING_INSTANT).asText());
        final String status = root.get(IssueStringHelper.STATUS).asText();
        final String droneId = root.get(IssueStringHelper.DRONE_ID).asText();
        if (root.has(IssueStringHelper.ID)) {
            final int id = root.get(IssueStringHelper.ID).asInt();

            if (IssueStringHelper.STATUS_OPEN.equals(status))
                return new OpenIssue(subject, id, details, courierUsername, assigneeUsername, sendingInstant, droneId);

            if (IssueStringHelper.STATUS_VISIONED.equals(status))
                return new VisionedIssue(subject, id, details, courierUsername, assigneeUsername, sendingInstant,
                        droneId);

            if (IssueStringHelper.STATUS_CLOSED.equals(status)) {
                final String solution = root.get(IssueStringHelper.SOLUTION).asText();
                return new ClosedIssue(subject, id, details, courierUsername, assigneeUsername, sendingInstant, droneId,
                        solution);
            }
        }

        return new SendingIssue(subject, details, courierUsername, assigneeUsername, sendingInstant, droneId);
    }
}
