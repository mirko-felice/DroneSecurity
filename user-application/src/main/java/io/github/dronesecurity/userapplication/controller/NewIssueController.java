/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttTopicConstants;
import io.github.dronesecurity.userapplication.reporting.issue.entities.SendingIssue;
import io.github.dronesecurity.userapplication.reporting.issue.services.CourierIssueReportService;
import io.github.dronesecurity.userapplication.utilities.UserHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

import java.time.Instant;

/**
 * Controller dedicated to manage the creation of an issue.
 */
public class NewIssueController {

    @FXML private TextField issueSubject;
    @FXML private TextArea infoTextArea;

    @FXML
    private void sendIssue() {
        final String issueInfo = this.infoTextArea.getText();
        final String subjectText = this.issueSubject.getText();
        final SendingIssue issue =
                new SendingIssue(subjectText,
                        issueInfo,
                        UserHelper.getLoggedUser().getUsername(),
                        Instant.now());
        CourierIssueReportService.getInstance().addIssueReport(issue).onComplete(ignored -> Platform.runLater(() -> {
            final JsonNode json = new ObjectMapper().createObjectNode()
                    .put(MqttMessageParameterConstants.ISSUE_REPORT_INFO_PARAMETER, issueInfo);
            Connection.getInstance().publish(MqttTopicConstants.ISSUE_TOPIC, json);
            try {
                ((Stage) this.infoTextArea.getScene().getWindow()).close();
            } catch (ClassCastException e) {
                LoggerFactory.getLogger(getClass()).error("Error closing the new window:", e);
            }
        }));
    }
}
