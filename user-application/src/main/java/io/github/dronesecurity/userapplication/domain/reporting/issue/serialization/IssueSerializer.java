/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.dronesecurity.lib.DateHelper;
import io.github.dronesecurity.userapplication.domain.reporting.issue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.entities.Issue;
import io.github.dronesecurity.lib.CastHelper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

/**
 * Serializes the {@link Issue}.
 */
public class IssueSerializer extends JsonSerializer<Issue> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final @NotNull Issue value, final @NotNull JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeStringField(IssueStringHelper.SUBJECT, value.getSubject());
        gen.writeStringField(IssueStringHelper.DETAILS, value.getDetails());
        gen.writeStringField(IssueStringHelper.COURIER, value.getCourier());
        gen.writeStringField(IssueStringHelper.ASSIGNEE, value.assignedTo());
        gen.writeStringField(IssueStringHelper.SENDING_INSTANT, DateHelper.toString(value.getReportingDate()));
        gen.writeStringField(IssueStringHelper.STATUS, value.getState());
        gen.writeStringField(IssueStringHelper.DRONE_ID, value.getDroneId());

        final Optional<ClosedIssue> closedIssue = CastHelper.safeCast(value, ClosedIssue.class);
        closedIssue.ifPresent(issue -> {
            try {
                gen.writeStringField(IssueStringHelper.SOLUTION, issue.getIssueSolution());
            } catch (IOException e) {
                LoggerFactory.getLogger(getClass()).error("Error occurred during serialization!", e);
            }
        });
    }
}
