/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.reporting.issue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.lib.connection.Connection;
import io.github.dronesecurity.lib.connection.MqttTopicConstants;
import io.github.dronesecurity.userapplication.application.reporting.issue.activeissue.ActiveIssueServiceImpl;
import io.github.dronesecurity.userapplication.application.reporting.issue.closedissue.ClosedIssueServiceImpl;
import io.github.dronesecurity.userapplication.domain.reporting.issue.activeissue.entities.AbstractActiveIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.activeissue.services.ActiveIssueService;
import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.services.ClosedIssueService;
import io.github.dronesecurity.userapplication.domain.reporting.issue.openissue.entities.OpenIssue;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

/**
 * Service that implements methods that retrieve specific issues, described in {@link DataIssueReportService}.
 */
public abstract class AbstractIssueReportRetrievalService implements IssueReportService {

    private final ActiveIssueService activeIssueService;
    private final ClosedIssueService closedIssueService;

    /**
     * Instantiates services that retrieve specific issues.
     */
    protected AbstractIssueReportRetrievalService() {
        this.activeIssueService = new ActiveIssueServiceImpl();
        this.closedIssueService = new ClosedIssueServiceImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AbstractActiveIssue> getActiveIssueReportsForCourier(final String username) {
        return this.activeIssueService.retrieveActiveIssuesForCourier(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AbstractActiveIssue> getActiveIssueReportsForAssignee(final String username) {
        return this.activeIssueService.retrieveActiveIssuesForAssignee(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClosedIssue> getClosedIssueReportsForCourier(final String username) {
        return this.closedIssueService.retrieveClosedIssuesForCourier(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClosedIssue> getClosedIssueReportsForAssignee(final String username) {
        return this.closedIssueService.retrieveClosedIssuesForAssignee(username);
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
