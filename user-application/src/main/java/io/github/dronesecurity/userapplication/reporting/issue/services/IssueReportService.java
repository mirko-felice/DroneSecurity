/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.issue.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttTopicConstants;
import io.github.dronesecurity.userapplication.auth.entities.LoggedUser;
import io.github.dronesecurity.userapplication.reporting.issue.entities.*;
import io.github.dronesecurity.userapplication.reporting.issue.repo.IssueReportRepository;
import io.github.dronesecurity.userapplication.utilities.UserHelper;
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
                Connection.getInstance().publish(MqttTopicConstants.ISSUE_TOPIC, json);
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(this.getClass()).warn("Can not deserialize correctly the issue.", e);
            }
        }).mapEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Boolean> visionIssue(final OpenIssue issue) {
        return REPOSITORY.visionOpenIssue(issue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Boolean> closeIssue(final VisionedIssue issue, final String solution) {
        return REPOSITORY.closeVisionedIssue(issue, solution);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void subscribeToNewIssue(final Consumer<OpenIssue> consumer) {
        Connection.getInstance().subscribe(MqttTopicConstants.ISSUE_TOPIC, mqttMessage -> {
            try {
                final OpenIssue issue = new ObjectMapper()
                        .readValue(new String(mqttMessage.getPayload(), StandardCharsets.UTF_8), OpenIssue.class);
                final LoggedUser loggedUser = UserHelper.logged();
                if (issue.assignedTo().equals(loggedUser.getUsername()))
                    consumer.accept(issue);
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(this.getClass()).warn("Can not deserialize issue.", e);
            }
        });
    }
}
