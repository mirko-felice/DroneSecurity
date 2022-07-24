/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.reporting.issue.creation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.lib.connection.Connection;
import io.github.dronesecurity.lib.connection.MqttTopicConstants;
import io.github.dronesecurity.userapplication.domain.reporting.issue.creation.entities.SendingIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.creation.repo.CreationRepository;
import io.github.dronesecurity.userapplication.domain.reporting.issue.creation.services.CreationService;
import io.github.dronesecurity.userapplication.infrastructure.reporting.issue.repo.creation.CreationRepositoryImpl;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link CreationService}.
 */
public class CreationServiceImpl implements CreationService {

    private final CreationRepository repository;

    /**
     * Instantiates the repository for this service.
     */
    public CreationServiceImpl() {
        this.repository = new CreationRepositoryImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIssueReport(final SendingIssue issue) {
        try {
            final JsonNode json = new ObjectMapper().readTree(this.repository.addIssue(issue).toString());
            Connection.getInstance().publish(MqttTopicConstants.ISSUE_TOPIC + issue.assignedTo(), json);
        } catch (JsonProcessingException e) {
            LoggerFactory.getLogger(this.getClass()).warn("Can not deserialize correctly the issue.", e);
        }
    }
}
