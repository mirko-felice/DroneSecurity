/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;


import io.github.dronesecurity.userapplication.auth.AuthenticationService;
import io.github.dronesecurity.userapplication.reporting.issue.entities.SendingIssue;
import io.github.dronesecurity.userapplication.reporting.issue.services.CourierIssueReportService;
import io.github.dronesecurity.userapplication.utilities.UserHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        AuthenticationService.getInstance().retrieveCourier(UserHelper.logged().getUsername()).onSuccess(courier -> {
            final SendingIssue issue =
                    new SendingIssue(subjectText,
                            issueInfo,
                            courier.getUsername(),
                            courier.getSupervisor(),
                            Instant.now());
            CourierIssueReportService.getInstance().addIssueReport(issue).onSuccess(ignored ->
                    Platform.runLater(((Stage) this.infoTextArea.getScene().getWindow())::close));
        });
    }
}
