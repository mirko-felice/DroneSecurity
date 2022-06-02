/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;

import io.github.dronesecurity.userapplication.reporting.issue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.reporting.issue.entities.CreatedIssue;
import io.github.dronesecurity.userapplication.reporting.issue.entities.Issue;
import io.github.dronesecurity.userapplication.reporting.issue.services.CourierIssueReportService;
import io.github.dronesecurity.userapplication.utilities.FXHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The controller of the issue creation service.
 */
public final class CourierIssueController implements Initializable {

    private static final String NEW_ISSUE_FXML = "newIssue.fxml";
    @FXML private TabPane issuesPane;

    // Issue Details visualization nodes
    @FXML private GridPane selectedIssuePane;
    @FXML private Label selectedIssueSubject;
    @FXML private Label issueState;
    @FXML private Label selectedIssueCreationDate;
    @FXML private Label selectedIssueCreationTime;
    @FXML private Text selectedIssueDetails;
    @FXML private Button newIssueButton;

    // Open issues list visualization nodes
    @FXML private ListView<CreatedIssue> openIssuesListView;
    @FXML private ListView<CreatedIssue> closedIssuesListView;

    private final CourierIssueReportService issueReportService;
    private final Map<Integer, CreatedIssue> openIssues;
    private final Map<Integer, ClosedIssue> closedIssues;

    /**
     * Instantiates the issue report controller with its service.
     */
    public CourierIssueController() {
        this.issueReportService = CourierIssueReportService.getInstance();

        this.openIssues = new HashMap<>();
        this.closedIssues = new HashMap<>();
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        final MultipleSelectionModel<CreatedIssue> openIssuesSelectionModel =
                this.openIssuesListView.getSelectionModel();
        openIssuesSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        openIssuesSelectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.newIssueButton.setVisible(false);
                final CreatedIssue issue = this.openIssues.get(newValue.getId());

                this.fillIssueFields(issue);
            }
        });

        final MultipleSelectionModel<CreatedIssue> closedIssuesSelectionModel =
                this.closedIssuesListView.getSelectionModel();
        closedIssuesSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        closedIssuesSelectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.newIssueButton.setVisible(false);
                final ClosedIssue issue = this.closedIssues.get(newValue.getId());

                this.fillIssueFields(issue);
            }
        });

        this.refreshOpenIssues();

        this.issueReportService.getClosedIssueReports().onComplete(res -> {
            if (res.succeeded()) {
                res.result().forEach(el -> this.closedIssues.put(el.getId(), el));
                final List<Integer> ids = new ArrayList<>(List.copyOf(this.closedIssues.keySet()));
                ids.sort(Integer::compareTo);
                final List<CreatedIssue> testIds = ids.stream().map(this.closedIssues::get)
                        .collect(Collectors.toList());
                this.closedIssuesListView.setItems(FXCollections.observableList(testIds));
            }
        });
    }

    @FXML
    private void goBackFromOpenIssues() {
        this.selectedIssuePane.setVisible(false);
        this.clearSelection();
        this.issuesPane.setVisible(true);
        this.newIssueButton.setVisible(true);
    }

    private void clearSelection() {
        this.openIssuesListView.getSelectionModel().clearSelection();
        this.closedIssuesListView.getSelectionModel().clearSelection();
    }

    private void fillIssueFields(final Issue issue) {
        this.selectedIssueSubject.setText(issue.getSubject());
        this.issueState.setText(issue.getState().substring(0, 1).toUpperCase(Locale.ITALY)
                + issue.getState().substring(1));
        final String creationInstant = issue.getReportingDate().toString();
        final String[] instantComponents = creationInstant.split("T");
        this.selectedIssueCreationDate.setText(instantComponents[0]);
        this.selectedIssueCreationTime.setText(instantComponents[1]
                .replace("Z", ""));
        this.selectedIssueDetails.setText(issue.getDetails());

        this.issuesPane.setVisible(false);
        this.selectedIssuePane.setVisible(true);
    }

    @FXML
    private void newIssue() {
        final FXMLLoader loader = new FXMLLoader(CourierIssueController.class.getResource(NEW_ISSUE_FXML));
        FXHelper.initializeWindow(Modality.WINDOW_MODAL, "Create new Issue", loader).ifPresent(stage -> {
            stage.initOwner(this.issuesPane.getScene().getWindow());
            stage.setOnHidden(ignored -> this.refreshOpenIssues());
            stage.showAndWait();
        });
    }

    private void refreshOpenIssues() {
        this.issueReportService.getOpenIssueReports().onComplete(res -> Platform.runLater(() -> {
            if (res.succeeded()) {
                res.result().forEach(el -> this.openIssues.put(el.getId(), el));
                final List<Integer> ids = new ArrayList<>(List.copyOf(this.openIssues.keySet()));
                ids.sort(Integer::compareTo);
                final List<CreatedIssue> testIds = ids.stream().map(this.openIssues::get).collect(Collectors.toList());
                this.openIssuesListView.setItems(FXCollections.observableList(testIds));
            }
        }));
    }
}
