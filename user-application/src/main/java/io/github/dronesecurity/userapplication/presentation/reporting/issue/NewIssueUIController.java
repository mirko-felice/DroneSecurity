/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.reporting.issue;

import io.github.dronesecurity.userapplication.application.reporting.issue.CourierIssueReportService;
import io.github.dronesecurity.userapplication.application.user.ohs.pl.Courier;
import io.github.dronesecurity.userapplication.domain.reporting.issue.creation.entities.SendingIssue;
import io.github.dronesecurity.userapplication.utilities.DialogUtils;
import io.github.dronesecurity.userapplication.utilities.user.UserAPIHelper;
import io.vertx.ext.web.codec.BodyCodec;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.Instant;

/**
 * Controller dedicated to manage the creation of an issue.
 */
public class NewIssueUIController {

    @FXML private TextField issueSubject;
    @FXML private TextArea infoTextArea;

    @FXML
    private void sendIssue() {
        final String issueInfo = this.infoTextArea.getText();
        final String subjectText = this.issueSubject.getText();
        UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_LOGGED_COURIER_IF_PRESENT, BodyCodec.json(Courier.class))
                .onSuccess(res -> {
                    final Courier courier = res.body();
                    Platform.runLater(() ->
                            DialogUtils.createDronePickerDialog("Choose the drone to report",
                                            courier.getAssignedDrones())
                                    .showAndWait().ifPresent(droneId -> {
                                        final SendingIssue issue =
                                                new SendingIssue(subjectText,
                                                        issueInfo,
                                                        courier.getUsername(),
                                                        courier.getSupervisorUsername(),
                                                        Instant.now(),
                                                        droneId);
                                        new CourierIssueReportService().addIssueReport(issue);
                                        Platform.runLater(((Stage) this.infoTextArea.getScene().getWindow())::close);
                            }));
        });
    }
}
