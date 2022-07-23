/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.lib.connection.Connection;
import io.github.dronesecurity.lib.connection.MqttTopicConstants;
import io.github.dronesecurity.userapplication.domain.reporting.issue.entities.*;
import io.github.dronesecurity.userapplication.domain.reporting.issue.repo.IssueReportRepository;
import io.vertx.core.Future;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

/**
 * The service for issue reporting.
 */
public final class IssueReportService implements CourierIssueReportService, MaintainerIssueReportService {

    private static final IssueReportRepository REPOSITORY = IssueReportRepository.getInstance();
    private static IssueReportService singleton;

    private IssueReportService() { }

    /**
     * Gets the singleton instance.
     * @return the singleton
     */
    public static IssueReportService getInstance() {
        synchronized (IssueReportService.class) {
            if (singleton == null)
                singleton = new IssueReportService();
            return singleton;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<List<CreatedIssue>> getOpenIssueReports() {
        return REPOSITORY.getOpenIssues();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<List<ClosedIssue>> getClosedIssueReports() {
        return REPOSITORY.getClosedIssues();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> addIssueReport(final Issue issue) {
        return REPOSITORY.addIssue(issue).onSuccess(newIssue -> {
            try {
                final JsonNode json = new ObjectMapper().readTree(newIssue.toString());
                Connection.getInstance().publish(MqttTopicConstants.ISSUE_TOPIC + issue.assignedTo(), json);
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(this.getClass()).warn("Can not deserialize correctly the issue.", e);
            }
        }).mapEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Boolean> visionIssue(final VisionedIssue issue) {
        return REPOSITORY.updateIssueState(issue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Boolean> closeIssue(final ClosedIssue issue) {
        return REPOSITORY.closeIssue(issue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void subscribeToNewIssue(final String maintainerUsername, final Consumer<OpenIssue> consumer) {
        Connection.getInstance().subscribe(MqttTopicConstants.ISSUE_TOPIC + maintainerUsername, mqttMessage -> {
            try {
                final OpenIssue issue = new ObjectMapper()
                        .readValue(new String(mqttMessage.getPayload(), StandardCharsets.UTF_8), OpenIssue.class);
                consumer.accept(issue);
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(this.getClass()).warn("Can not deserialize issue.", e);
            }
        });
    }
}
